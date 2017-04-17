####上传热点新闻
_Request_
```json
POST /v1/hot/news
Host: 10.117.191.225
Content-Type: application/x-www-form-urlencoded
```
| Key  | 参数类型   | 是否必须     | 参数解释 |
| ---- | :----- | :------- | :--- |
| news  | String | 是        | 热点新闻集合json串 |

| 错误编码  | 错误说明   |
| ---- | :----- | 
| 4015  | Request Header ContentType Not Supported | 
| 4002  | InvalidArgument | 
| 2000  | Upload Hot News Success | 
