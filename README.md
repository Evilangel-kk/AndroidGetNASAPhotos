# AndroidGetNASAPhotos
## 安卓系统制作获取NASA官网的图片并且展示出来的APP
- HTTP请求获得json数据
- jsonObject解析json数据包，得到图片的信息
- 将图片的id和url保存到SQLite中，进行持久化存储
- 通过Picasso将图片进行缓存并展示到网格布局中
