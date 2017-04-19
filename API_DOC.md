####上传热点新闻
_Request_
```json
POST /v1/news/hot
Host: 10.117.191.225
Content-Type: application/x-www-form-urlencoded
{
	"news":"雄安新区"
}
```
| Key  | 参数类型   | 是否必须     | 参数解释 |
| ---- | :----- | :------- | :--- |
| news  | String[ ] | 是        | 热点新闻集合 |

_Response_

```json
HTTP/1.1 200 OK
Content-Type: application/json
{
  "code": 2000,
  "message": "Upload Hot News Success"
}
```
| code  | 编码说明   |
| ---- | :----- | 
| 4015  | Request Header ContentType Not Supported | 
| 4002  | InvalidArgument | 
| 2000  | Upload Hot News Success | 
