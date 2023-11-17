import socket as sock
import json
from threading import Thread
class DataPort :
    chunk=4096
    def __init__(self):
        self.con=sock.socket()
    def receive(self):
        totaldata=str()
        while True:
            temp=self.con.recv(self.chunk).decode("utf-8")
            totaldata+=temp
            if len(temp)<self.chunk:
                break
        return totaldata.replace("\n","")
    def send(self,data):
        data=bytes(data+"\n","utf-8")
        totalsent=0
        while totalsent<len(data):
            totalsent+=self.con.send(data[totalsent:])
    def write(self,data:dict):
        self.send(json.dumps(data))
    def read(self)->dict:
        return json.loads(self.receive())
class Client (DataPort ):
    def __init__(self,port:int):
        self.con=sock.socket(sock.AF_INET,sock.SOCK_STREAM)
        self.con.connect(("127.0.0.1",port))
    def request (self,context:str,data:dict)->dict:
        self.send(context)
        if context=="<close>":
            self.con.close()
            return
        status=self.receive()
        if status=="ok":
            self.write(data)
            return self.read()
class Server(DataPort ):
    def __init__(self):
        self.handlers=dict()
    def __serve(self,port:int):
        server=sock.socket (sock.AF_INET,sock.SOCK_STREAM)
        server.setsockopt(sock.SOL_SOCKET,sock.SO_REUSEADDR,1)
        server.bind(("",port))
        server.listen(5)
        while True:
            self.con,addr=server.accept()
            while True:
                h= self.receive()
                if h=="":
                    self.con.close()
                    break
                handler=self.handlers[h]
                if handler:
                    self.send("ok")
                    self.write(handler(self.read()))

    def serve(self,port:int):
        Thread(target=self.__serve,args=(port,)).start()
    def addContext(self, context:str,handler):
        self.handlers[context]=handler