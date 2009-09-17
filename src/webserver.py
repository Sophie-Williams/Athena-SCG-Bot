#!/usr/bin/env python
import BaseHTTPServer


class RequestHandler(BaseHTTPServer.BaseHTTPRequestHandler):

    def do_something(self, handler):
        print self.rfile.read()
        status, data = handler()

        if status == 200:
            self.send_response(200)
        else:
            self.send_error(status)
            return
        self.send_header('Content-Length', len(data))
        self.end_headers()

        self.wfile.write(data)

    def do_GET(self):
        self.do_something(self.handle_get_request)

    def do_POST(self):
        self.do_something(self.handle_post_request)

    def handle_get_request(self):
        try:
            result = {
                '/player': self.answer,
                '/start': lambda x: 'OK',
                '/end': lambda x: 'OK',
            }[self.path]()

        except: # KeyError
            return 404, 'Unknown Request'
        return 200, result

    def handle_post_request(self):
        return 200, self.rfile.read()

    def answer(self):
        context = self.create_context()

    def create_context(self):
        return self.rfile.read()


def run(server_class=BaseHTTPServer.HTTPServer,
        handler_class=RequestHandler, address='', port=8000):
    server_address = (address, port)
    httpd = server_class(server_address, handler_class)
    httpd.serve_forever()


if __name__ == '__main__':
    run()
