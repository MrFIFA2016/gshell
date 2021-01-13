import socket               # 导入 socket 模块
 
s = socket.socket()         # 创建 socket 对象
host = '127.0.0.1' # 获取本地主机名
port = 12345                # 设置端口
s.bind((host, port))        # 绑定端口
 
s.listen(5)                 # 等待客户端连接
while True:
    c,addr = s.accept()     # 建立客户端连接
    print ('连接地址：', addr)
    while True:
        try:
            data = c.recv(1024)  #接收数据
            data = data.decode('utf-8')
            print('recive:',data) #打印接收到的数据
            c.send(('welcome!! '+ data).encode('utf-8'))
        except Exception as e:
            print('关闭了正在占线的链接！',e)
            break
    c.close()             
