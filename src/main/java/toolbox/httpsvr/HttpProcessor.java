package toolbox.httpsvr;

public class HttpProcessor {
    // HTTP服务根目录

    public void process(HttpRequest request, HttpResponse response) {
//        String uri = request.getRemoteURI();
        String content = "im magic";
//        response.write("HTTP/1.1 200 OK\n");
//        response.write("content-length: " + content.length() + "\n");
//        response.write("content-type: text/plain; charset=UTF-8 \n\n");
//        response.write(content);
        String headerAndContent = "HTTP/1.1 200 OK\n"
                + "content-length: " + content.length() + "\n"
                + "content-type: text/plain; charset=UTF-8 \n\n"
                + content;
        response.write(headerAndContent);
    }
}