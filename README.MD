# Foxglove WebSocket Protocol in Java

Base on official document, Foxglove websocket protocol in Java has been implemented.

This repository provides a protocol specification and reference implementations enabling Foxglove Studio to ingest arbitrary “live” streamed data.

A Foxglove WebSocket server can provide multiple data streams, called channels. When a client subscribes to a channel, it begins receiving messages on that channel. This protocol does not prescribe the messages' data format. Instead, the server specifies each channel's encoding, and the client uses this information to determine whether it can decode that channel's messages. Read the Foxglove Studio documentation for more information on which encodings Studio supports.

Implementations are available in the following languages:


|  Language   | Includes  | Package name |
|  ----  | ----  | ----| 
| Python  | server + examples | foxglove-websocket |
| JavaScript/TypeScript  | server + client | @foxglove/ws-protocol |
| JavaScript/TypeScript  | examples | @foxglove/ws-protocol-examples |
| C++  | server + examples | foxglove-websocket |


## reference

> https://github.com/foxglove/ws-protocol


## RoadMap

- 接收用户的设置信息
- Panel： 3D， Plot， Image， Table， Map(要实现这五种组件的信息发送)
- Schema：(将第三方的数据基于schema转化foxglove数据)

## RTSP相关指令
* 生成视频流
```bash
ffmpeg -re -i E:\foxglove\video\test_video.mp4 -vcodec libx264 -acodec aac -f flv rtmp://127.0.0.1:1935/demo
ffmpeg -re -i E:\foxglove\video\bmw_drive.mp4 -vcodec libx264 -acodec aac -f flv rtmp://127.0.0.1:1935/demo
ffmpeg -re -i E:\foxglove\video\usa_drive_2.mp4 -vcodec libx264 -acodec aac -f flv rtmp://127.0.0.1:1935/demo1
ffmpeg -re -i E:\foxglove\video\chongqi_drive.mp4 -vcodec libx264 -acodec aac -f flv rtmp://127.0.0.1:1935/demo2
```
* HTTP访问地址： `http://127.0.0.1:8888/demo`

## leaflet地图 
* 高德 `https://webrd04.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=7&x={x}&y={y}&z={z}`
* openstreet `https://tile.openstreetmap.org/{z}/{x}/{y}.png`
* 智图地图 `https://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/tile/{z}/{y}/{x}`
