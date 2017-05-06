package spark;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

class FakeHttpServletRequest implements HttpServletResponse {
    private int statusCode;
    private String statusMessage;

    @Override
    public void addCookie(final Cookie cookie) {

    }

    @Override
    public boolean containsHeader(final String name) {
        return false;
    }

    @Override
    public String encodeURL(final String url) {
        return null;
    }

    @Override
    public String encodeRedirectURL(final String url) {
        return null;
    }

    @Override
    public String encodeUrl(final String url) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(final String url) {
        return null;
    }

    @Override
    public void sendError(final int sc, final String msg) throws IOException {

    }

    @Override
    public void sendError(final int sc) throws IOException {

    }

    @Override
    public void sendRedirect(final String location) throws IOException {

    }

    @Override
    public void setDateHeader(final String name, final long date) {

    }

    @Override
    public void addDateHeader(final String name, final long date) {

    }

    @Override
    public void setHeader(final String name, final String value) {

    }

    @Override
    public void addHeader(final String name, final String value) {

    }

    @Override
    public void setIntHeader(final String name, final int value) {

    }

    @Override
    public void addIntHeader(final String name, final int value) {

    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setStatus(final int sc) {
        this.statusCode = sc;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void setStatus(final int sc, final String sm) {
        this.statusCode = sc;
        this.statusMessage = sm;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public int getStatus() {
        return this.statusCode;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return null;
    }

    @Override
    public String getHeader(final String name) {
        return null;
    }

    @Override
    public void setContentType(final String type) {

    }

    @Override
    public Collection<String> getHeaders(final String name) {
        return null;
    }

    @Override
    public void setCharacterEncoding(final String charset) {

    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    @Override
    public void setContentLength(final int len) {

    }

    @Override
    public void setContentLengthLong(final long len) {

    }

    @Override
    public void setBufferSize(final int size) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(final Locale loc) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
