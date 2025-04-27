# Spring Boot QUIC

```shell
# /etc/hosts
# match the certificate domain
127.0.0.1 video.markruler.com
```

```shell
nohup java -jar quic-0.1.0.jar \
	--spring.ssl.bundle.pem.server-http3.keystore.certificate=file:${CRT_PEM} \
	--spring.ssl.bundle.pem.server-http3.keystore.private-key=file:${KEY_PEM} \
	> server.out 2>&1 &
```

```shell
sudo docker run -it --rm --platform linux/arm64 alpine/curl-http3 curl --max-time 3 --http3 -IL https://video.markruler.com:15201/string
```

```shell
sudo docker run -it --rm \
  --memory=4g \
  --memory-swap=4g \
  -v $HOME/Videos:/videos \
  --platform linux/arm64 \
  alpine/curl-http3 \
  curl \
  --connect-timeout 3 \
  --http3 \
  -X POST \
  https://video.markruler.com:15201/video/binary \
  --header 'Content-Type: application/octet-stream' \
  --header 'filename: t' \
  --upload-file '/videos/610mb.mp4' \
  -w "Total time: %{time_total}\n"
```
