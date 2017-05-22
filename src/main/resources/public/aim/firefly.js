let hostPort = location.hostname + ":" + location.port;
let isDrawing = false;
let webSocket = null;
webSocket = setupStatus();
let deck_al = new Deck("/alliance/nav");
let deck_bo = new Deck("/border/nav");
let deck_ri = new Deck("/rim/nav");
let decks = new Map();
decks.set(deck_al.navPath, deck_al);
decks.set(deck_bo.navPath, deck_bo);
decks.set(deck_ri.navPath, deck_ri);
let dd_al = new DeckDisplay(deck_al);
let dd_bo = new DeckDisplay(deck_bo);
let dd_ri = new DeckDisplay(deck_ri);
let dds = new Map();
dds.set(deck_al, dd_al);
dds.set(deck_bo, dd_bo);
dds.set(deck_ri, dd_ri);

let actionCheckboxes = ["workCb", "buyCb", "dealCb", "flyCb"];


function Deck(navPath) {
    this.navPath = navPath;
    this.navLockPath = navPath + "/lock";
    this.specPath = navPath + "/spec";
    this.remainingCardsCount = -1;
    this.discardsCount = -1;
    this.isLocked = false;
    this.hasCards = false;
    this.nextDrawDisabledStatus = false;
    this.shouldShuffle = false;
}
Deck.FindAndUpdateFrom = function(statusJsonString) {
    let statusj = JSON.parse(statusJsonString);
    let deckName = statusj.source;
    let deck = decks.get(deckName);
    deck.remainingCardsCount = statusj.remainingCardsCount;
    deck.discardsCount = statusj.discardsCount;
    deck.isLocked = statusj.isLocked;
    deck.hasCards = deck.remainingCardsCount > 0;
    deck.nextDrawDisabledStatus = !deck.hasCards || deck.isLocked;
    deck.shouldShuffle = !(deck.hasCards || deck.isLocked);
    return deck;
}
Deck.prototype.matches = function(path) {
    return (this.navPath==path) || (this.navLockPath==path) || (this.specPath==path);
}


function Card(deck, action) {
    this.deck = deck;
    this.action = action;
    let cardName = this.action.replace(/[^a-zA-Z]/g, "") + ".png";
    cardName = startWithSlash(cardName);
    this.url = "http://" + hostPort + this.deck.navPath + "Card" + cardName;
}
Card.FromJson = function(deck, cardJsonString) {
    let cardj = JSON.parse(cardJsonString);
    return new Card(deck, cardj.action);
}
Card.prototype.getAnchor = function(anchorText) {
    return "<a href='" + this.url + "' target='_blank'>" + anchorText + "</a>";
}

function DeckDisplay(deck) {
    this.deck = deck;
    this.responseStatusArea = deck.navPath + "/requestResult";
    this.drawBtnId = this.deck.navPath + "/drawBtn";
    this.shuffleBtnId = this.deck.navPath + "/shuffleBtn";
    this.unlockBtnId = this.deck.navPath + "/unlockBtn";
    this.statusId = this.deck.navPath + "/status";
    this.specAreaId = this.deck.navPath + "/spec";
}
DeckDisplay.updateStatus = function(msg) {
    let deck = Deck.FindAndUpdateFrom(msg.data);
    let displayDeck = dds.get(deck);
    displayDeck.reportStatus();
    displayDeck.applyStatus();
}
DeckDisplay.prototype.reportError = function(message) {
    updateElement(this.responseStatusArea, true, message);
}
DeckDisplay.prototype.reportUnexpectedError = function(xhttp) {
    let result = "Unexpected error: ReadyState, status, text: " + xhttp.readyState + "- " + xhttp.status + "-" +
        xhttp.statusText + ".";
    this.reportError(result);
}
DeckDisplay.prototype.updateResponse = function(message) {
    updateElement(this.responseStatusArea, false, message);
}
DeckDisplay.prototype.disableDrawCardButton = function(newState) {
    let control = document.getElementById(this.drawBtnId);
    if (newState) control.classList.add('disabled');
    else control.classList.remove('disabled');
}
DeckDisplay.prototype.applyNextDrawDisabledStatus = function() {
    //TODO how to make private methods (and then can make disable(id) private too)
    this.disableDrawCardButton(this.deck.nextDrawDisabledStatus);
}
DeckDisplay.prototype.applyStatus = function() {
    if (!isDrawing) this.applyNextDrawDisabledStatus();
    document.getElementById(this.shuffleBtnId).disabled = !this.deck.shouldShuffle;
    document.getElementById(this.unlockBtnId).disabled = !this.deck.isLocked;
    let responseText = document.getElementById(this.responseStatusArea).innerHTML;
    if(responseText != undefined && responseText.includes("shuffling") && !this.deck.shouldShuffle)
        updateElement(this.responseStatusArea, false, "");
    if(responseText != undefined && responseText.includes("Lock") && !this.deck.isLocked)
        updateElement(this.responseStatusArea, false, "");
}
DeckDisplay.prototype.reportStatus = function() {
    let result = this.deck.remainingCardsCount + "/" + (this.deck.remainingCardsCount + this.deck.discardsCount);
    result += "&nbsp;(" + this.deck.discardsCount + ")";
    updateElement(this.statusId, false, result);
}
DeckDisplay.prototype.displayCardBack = function() {
    let card = new Card(this.deck, "back");
    let cardDisplay = new CardDisplay(card);
    cardDisplay.display(0, 0, true);
}
DeckDisplay.prototype.matches = function(path) {
    return this.deck.matches(path);
}

function CardDisplay(card) {
    this.card = card;
}
CardDisplay.prototype.display = function(teaseTimeMs, postDisplayWaitTimeMs, hasMoreDrawingToDo) {
    let cardUrl = this.card.url;
    let altText = this.card.action;
    let qm = quoteMarkFor(altText);
    let newHtml = "<img class='external_image large_card_image' src='" + cardUrl + "'" + " alt=" + qm + altText +
        qm + "/>";
    let startOfWait = (new Date()).getTime();
    preloadImage(cardUrl, async function() {
            let endOfWait = (new Date()).getTime();
            let timeToSleep = Math.max(0, teaseTimeMs - (endOfWait - startOfWait));
            await sleep(timeToSleep);
            updateElement("/nav/card", false, newHtml);
            await sleep(Math.max(0, postDisplayWaitTimeMs));
            finishRequest(hasMoreDrawingToDo);
        });
}

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

function quoteMarkFor(text) {
    let qm = '"';
    if (text.includes(qm)) qm = "'";
    return qm;
}

function sleep(timeToSleepMs) {
    return new Promise(resolve => setTimeout(resolve, timeToSleepMs));
}

function preloadImage(url, callback) {
    let img = new Image();
    img.src = url;
    img.onload = callback;
}

function startWithSlash(text) {
    if ((text) && (!text.startsWith("/"))) text = "/" + text;
    return text;
}

function getUrl(path) {
    path = startWithSlash(path);
    return "http://" + hostPort + path;
}

function updateElement(targetId, addEmphasis, newHtml) {
    if (addEmphasis) newHtml = "<font color='red'>" + newHtml + "</font>";
    document.getElementById(targetId).innerHTML = newHtml;
}

function webSocketState(socketToCheck) {
    return socketToCheck==null?"undefined": socketToCheck.readyState;
}

function setupStatus() {
    if (webSocket != null) {
        let socketToCheck = webSocket;

        if(isWebSocketOpen(socketToCheck) ) {
            console.log("setupStatus false alarm. returning");
            return;
        } else {
            console.log("after check setupStatus ws rs, status? "
                + (socketToCheck==null?"undefined": socketToCheck.readyState));
            if (socketToCheck != null) socketToCheck.close(3000, "not open");
            console.log("closed socket");
        }
    }

    let newSocket = new WebSocket("ws://" + hostPort + "/status");

    newSocket.onmessage = function(msg) {
        console.log("websocket message from " + msg.origin + " " + webSocketState(this));
        DeckDisplay.updateStatus(msg);
    };
    newSocket.onclose = async function() {
        showOverlay("#balladOverlay");
     }
    newSocket.onopen = function(e) {
        console.log("WebSocket connection open. Asking for status update. " + this.readyState);
        window.location.href="#";
        this.send('update status');
         //TODO do something about repeating over all decks
        forEachDeckDisplay(function(value, key, map) {composeDescription(value);});

    };
    newSocket.onerror = function(e) {showOverlay("#balladOverlay"); };
    return newSocket;
}

function tryStatusAgain() {
    webSocket = setupStatus();
    if (isWebSocketOpen(webSocket)) {
        location.href="#";
        return;
    }
}

function isWebSocketOpen(socketToCheck) {
    let isWebSocketOpen = (socketToCheck != null) && socketToCheck.readyState == 1;
    return isWebSocketOpen;
}

function statusResetRequired(path) {
   if(isWebSocketOpen(webSocket)) return false;

   webSocket = setupStatus();
   if(!isWebSocketOpen(webSocket)) {
       showOverlay("#balladOverlay");
       return true;
   }

   return false;
  }

function sendRequest(method, path, onLoadCallback, onErrorCallback) {
    if (statusResetRequired(path)) return;

    let xhttp = new XMLHttpRequest();
    xhttp.addEventListener("load", function(ev) { onLoadCallback(xhttp); });
    xhttp.addEventListener("error", function(ev) {  onErrorCallback(xhttp);   });
    xhttp.open(method, getUrl(path), true);
    //	  xhttp.setRequestHeader("Authorize", "dm9yZGVsOnZvcmRlbA==");
    xhttp.send();
}

function get(path, onLoadCallback, onErrorCallback) {
    sendRequest("GET", path, onLoadCallback, onErrorCallback);
}

function post(path, onLoadCallback, onErrorCallback) {
    sendRequest("POST", path, onLoadCallback, onErrorCallback);
}

function del(path, onLoadCallback, onErrorCallback) {
    sendRequest("DELETE", path, onLoadCallback, onErrorCallback);
}

function processActionResponse(xhttp, deckDisplay, successCallback) {
    let requestStatusCode = xhttp.status;

    switch (requestStatusCode) {
        case 200:
            successCallback(deckDisplay, xhttp.responseText)
            updateRequestId(xhttp.getResponseHeader("id"));
            return;
        case 404:
            deckDisplay.reportError("Needs shuffling!");
            break;
        case 409:
            deckDisplay.reportError("Locked!");
        default:
            deckDisplay.reportUnexpectedError(xhttp);
    }
    finishRequest(false);
}

function finishRequest(hasMoreDrawingToDo) {
    if(isDrawing) isDrawing=hasMoreDrawingToDo;
    applyStatusForAllDecks();
}

function updateRequestId(id) {
    let target = "requestId";
    updateElement(target, false, id);
}

function restoreHistory() {
    let history = localStorage.getItem("turnHistory");
    updateElement("history", false, history);
}

function setHistory(history) {
    updateElement("history", false, history);
    localStorage.setItem("turnHistory", history);
}

function prependHistoryWith(newItem) {
    let history = "<li>" +getTimestamp() + " " + newItem + "</li>" + document.getElementById("history").innerHTML;
    setHistory(history);
}

function getTimestamp() {
    return new Date().toLocaleTimeString('en-US', {hour12: false });
}
function updateHistory(card) {
    let sector = card.deck.navPath.replace("/", "");
    sector = sector.replace("/nav", "");
    sector = capitalizeFirstLetter(sector);
    let anchorText = sector + "-" + card.action;
    prependHistoryWith(card.getAnchor(anchorText) );
}

function forEachDeckDisplay(valueKeyMap) {
    dds.forEach(valueKeyMap);
}

function disableDrawButtons() {
    forEachDeckDisplay(function(value, key, map) {
        value.disableDrawCardButton(true);
    });
}

function applyStatusForAllDecks() {
    forEachDeckDisplay(function(value, key, map) {
        value.applyStatus();
    });
}

function displayNavCard(deckDisplay, responseText) {
    let card = Card.FromJson(deckDisplay.deck, responseText);
    let cardDisplay = new CardDisplay(card);
    cardDisplay.display(500, 500, false);
    deckDisplay.updateResponse("&nbsp;");
    updateHistory(card);
}

function justDisplayResponse(deckDisplay, responseText) {
    forEachDeckDisplay(function(value, key, map) {value.updateResponse("&nbsp;");});
    deckDisplay.updateResponse(responseText);
}

function displayCardsInOverlay(deckDisplay, responseText) {
    let html = "<table class='border-on-bottom'>";
    let cards = JSON.parse(responseText);
    for (let i = 0; i < cards.length; ++i) {
        let cardCount = cards[i];
        let card = new Card(deckDisplay.deck, cardCount.key);
        html += "<tr><td class='align_right'>" + card.getAnchor(card.action) +
            "</td><td class='align_right px80wide'>" + cardCount.value + "&nbsp;</td></tr>";
    }
    html += "</table>";
    updateElement(deckDisplay.specAreaId, false, html);
}

function createOnSendErrorCallback(deckDisplay) {
    return function(xhttp) {
        deckDisplay.reportUnexpectedError("Send failed.");
        finishRequest();
    };
}

function createOnSendLoadCallback(deck, successCallback) {
        return function(xhttp) {
            return processActionResponse(xhttp, deck, successCallback);
        };
    }

    //TODO put drawCard et. al on DeckDisplay when i figure out private functions (which most of these shoudl be)
    //     or is this now modules?
function drawCard(deckDisplay) {
    recordFlyAction();
    if (!continueIfMoreThan2Actions(document.getElementById("flyCb"))) return;

    let deck = deckDisplay.deck;
    isDrawing = true;
    disableDrawButtons();
    deckDisplay.displayCardBack();
    get(deck.navPath, createOnSendLoadCallback(deckDisplay, displayNavCard), createOnSendErrorCallback(
        deckDisplay));
}

function lock(deckDisplay) {
    post(deckDisplay.deck.navLockPath, createOnSendLoadCallback(deckDisplay, justDisplayResponse),
        createOnSendErrorCallback(deckDisplay));
}

function unlock(deckDisplay) {
    del(deckDisplay.deck.navLockPath, createOnSendLoadCallback(deckDisplay, justDisplayResponse),
        createOnSendErrorCallback(deckDisplay));
}

function shuffle(deckDisplay) {
    if (deckDisplay.deck.remainingCardsCount <= 0 || (confirm("Are you sure you want to shuffle?")))
        shuffleDontAsk(deckDisplay);
}

function shuffleDontAsk(deckDisplay) {
    post(deckDisplay.deck.navPath, createOnSendLoadCallback(deckDisplay, justDisplayResponse),
        createOnSendErrorCallback(deckDisplay));
}

function resetAll() {
    if (confirm("Are you sure you want to shuffle all decks?"))
        forEachDeckDisplay(function(value, key, map) {  shuffleDontAsk(value);  });
}

function clearHistory() {
    setHistory("");
}

function actionsTaken() {
    let counter = 0;
    for(let i = 0; i < actionCheckboxes.length; ++i) {
        let checkbox=document.getElementById(actionCheckboxes[i]);
        if (checkbox.checked) counter++;
    }
    return counter;
}

function startTurn() {
    if (actionsTaken() < 2 && (!confirm("Fewer than 2 actions. Are you sure?"))) return;

    //TODO use closure to dry looping over checkboxes.
    for(let i = 0; i < actionCheckboxes.length; ++i)
        document.getElementById(actionCheckboxes[i]).checked = false;

    prependHistoryWith("Start Turn" + "<hr class='half-line'>");
    updateElement("/nav/card", false,
        "<img src='/SerenityLogo.png' class='external_image logo_image'>");
}

function recordFlyAction() {
    document.getElementById("flyCb").disabled=false;
    document.getElementById("flyCb").checked=true;
    document.getElementById("flyCb").disabled=true;
}

function continueIfMoreThan2Actions(checkbox) {
    if (actionsTaken() > 2 && (!confirm("More than 2 actions. Are you sure?"))) {
        checkbox.checked = false;
        return false;
    }
    return true;
}

function recordAction(checkbox) {
    let marker = checkbox.name;
    if (!checkbox.checked) {
        prependHistoryWith("Un-" + marker);
        return;
    }

    if (!continueIfMoreThan2Actions(checkbox)) return;

    prependHistoryWith(marker);
}

function composeDescription(deckDisplay) {
    let current = document.getElementById(deckDisplay.deck.specPath).innerHTML;
    if (!current || current == "")
        get(deckDisplay.deck.specPath, createOnSendLoadCallback(deckDisplay, displayCardsInOverlay),
            createOnSendErrorCallback(deckDisplay));
}

function debounce(fun, milliSeconds) {
    var timer;
    return function() {
        clearTimeout(timer);
        timer = setTimeout(function() {
            fun();
        }, milliSeconds);
    };
}

function showOverlay(hrefToShow) {
    if (location.hash == hrefToShow) return;
    debounce(function() {
        document.querySelector('body').classList.add('noscroll');
        document.querySelector('html').classList.add('noscroll');
        location.href = hrefToShow;
    }, 200).call();
}

function restoreScrolling() {
            document.querySelector('body').classList.remove('noscroll');
            document.querySelector('html').classList.remove('noscroll');
}

function restoreScrollingAfterClosingOverlays() {
     let anchors = document.getElementsByClassName("close");
     for(let i = 0; i < anchors.length; i++) {
        let anchor = anchors[i];
        anchor.addEventListener("click",restoreScrolling(),false);
     }

     window.addEventListener('popstate', function(event) {
      let fragment = document.location.hash;
      if (""==fragment) restoreScrolling();
    }, true);

}

window.onerror = function (msg, url, lineNo, columnNo, error) {
    var string = msg.toLowerCase();
    var substring = "script error";
    if (string.indexOf(substring) > -1){
        alert('Script Error: See Browser Console for Detail');
    } else {
        var message = [
            'Message: ' + msg,
            'URL: ' + url,
            'Line: ' + lineNo,
            'Column: ' + columnNo,
            'Error object: ' + JSON.stringify(error)
        ].join(' - ');

        alert(message);
    }

    return false;
};

window.onload = async function(e) {
    restoreHistory();
    restoreScrollingAfterClosingOverlays();
  //  alert("user agent " +  navigator.userAgent);
 };