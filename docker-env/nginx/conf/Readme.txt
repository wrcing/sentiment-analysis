nginx.conf是一开始读取的文件配置，里面可以include，导进conf.d文件夹下的配置

nginx的https配置：

    server {
        listen       443 ssl;
        server_name  wrcing.com www.wrcing.com;

        charset utf-8;

        access_log  /var/log/nginx/mqtian.com.log; # 自定义日志输出

        ssl_certificate      /home/cert/mqtian.com/mqtian.com.pem; # SSL证书路径
        ssl_certificate_key  /home/cert/mqtian.com/mqtian.com.key; # SSL证书路径

        ssl_session_cache    shared:SSL:1m;
        ssl_session_timeout  5m;

        ssl_ciphers  HIGH:!aNULL:!MD5;
        ssl_prefer_server_ciphers  on;

        location / {
            #proxy_pass http://10.0.4.14:8662; # 后端地址
            #client_max_body_size 10M;
            #add_header Content-Security-Policy upgrade-insecure-requests; # 给站点http来源强制改变为https来源
            #proxy_set_header Host $http_host; # Host包含客户端真实的域名和端口号
            #proxy_set_header X-Real-IP $remote_addr; # X-Forwarded-Proto表示客户端真实的协议（http还是https）
            #proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; # X-Real-IP表示客户端真实的IP
            #proxy_set_header X-Forwarded-Proto $scheme;
            root   /usr/share/nginx/html; # 发布web项目的目录
            index index.html index.html;
        }

        error_page   500 502 503 504 /50x.html;
        location = /50x.html {
            root   /usr/share/nginx/html;
        }

        wrc:
                error_page   500 502 503 504 /50x.html;
                location = /50x.html {
                    root   /home/www/pages-4x-5x;
                }
   }
