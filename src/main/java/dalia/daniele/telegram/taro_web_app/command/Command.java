package dalia.daniele.telegram.taro_web_app.command;

public interface Command<Request, Response> {
    Response execute(Request request);
}
