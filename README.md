# OkRequester
基于okhttp封装的网络请求库


### step1: Add it in your root build.gradle at the end of repositories:

    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

### step2: Add the dependency

    dependencies {
	        implementation 'com.github.jianyuyouhun:OkRequester:2.1.1//useAndroidX
	        //implementation 'com.github.jianyuyouhun:OkRequester:2.0.1'//Not in use AndroidX
	}

	新版本均使用AndroidX依赖，2.0.1版本未使用AndroidX

## 使用 ##

支持常用http请求方式，内置json返回处理和xml返回处理。

以json返回结果的api调用为例，url为`www.test.com`

定义请求体基类：BaseHttpRequester


	@RequestMethod(HttpMethod.POST)//定义请求方式，子类可覆盖,加上isFinal = true可避免子类覆盖，比如multipart类型的文件上传，可见MultiPartUploadRequester
	@BodyCreator(JsonBodyCreator::class)//定义参数封装方式
	abstract class BaseHttpRequester<T>(
	    listener: OnResultListener<T>
	) :
	    BaseJsonRequester<T>(listener) {
	
	    override fun preHandleRequest(reqBuilder: Request.Builder) {
	        super.preHandleRequest(reqBuilder)
	        //请求发起前处理，在这里添加header等操作
	    }
	
	    override fun getApi(): ApiInterface = ApiInterface { 
			//api 请求连接
			"http://www.test.com/"
		}
	
	    override fun onResult(code: Int, `in`: JSONObject?, msg: String?) {
	        super.onResult(code, `in`, msg)
			//默认api返回中包含code,data,msg字段，无缺省，如果有则需要重写此方法定义，详情可见super实现
	    }
	
	    override fun parseCode(jsonObject: JSONObject): Int {
			return super.parseCode(jsonObject)
			//默认api返回code名为code，如果有其他定义，则重写，比如{"c":"1",...}
			// return jsonObject.optInt("c")
	    }
	
	    override fun parseMessage(jsonObject: JSONObject): String {
	    	return super.parseMessage(jsonObject)
			//默认api返回message名为msg，如果有其他定义，则重写，比如{...,"message":"success",...}
			// return jsonObject.optString("message")
		}
	}

使用：拉取翻页信息，路由为`api/list`

	@RequestMethod(HttpMethod.GET)
	@Route("api/list")
	class GetMeetingListRequester(
	    private val pageNum:Int,
	    private val pageSize:Int,
	    listener: OnResultListener<List<Info>>) :
	    BaseHttpRequester<List<Info>>(listener, false) {
	    override fun onDumpData(`in`: JSONObject): List<Info>? {
	        return JsonUtil.toList(`in`.getJSONArray("data"), Info::class.java)
	    }
	
	    override fun onPutParams(params: MutableMap<String, Any>) {
	        params["pageNum"] = pageNum
	        params["pageSize"] = pageSize
	    }
	}

	
`@Route`注解用于注入路由，如果参数是拼接在url里，比如`http://www.test.com/api/list/{page}/{pageSize}`，则需要重写setRoute方法直接返回路由拼接参数字段即可

	@RequestMethod(HttpMethod.GET)
	//@Route("api/list")
	class GetMeetingListRequester(
	    private val pageNum:Int,
	    private val pageSize:Int,
	    listener: OnResultListener<List<Info>>) :
	    BaseHttpRequester<List<Info>>(listener, false) {
	    override fun onDumpData(`in`: JSONObject): List<Info>? {
	        return JsonUtil.toList(`in`.getJSONArray("data"), Info::class.java)
	    }
	
	    override fun onPutParams(params: MutableMap<String, Any>) {
			//do nothing	   
	    }
	
	    override fun setRoute(): String {
	        return "api/list/$pageNum/$pageSize"
	    }
	}

页面调用：

	GetMeetingListRequester(OnResultListener<Info> { code, info, msg -> 
		//code为自定义值，结合实际进行
		when(code) {
			0->{}
			1->{}
			else->{}
		}
	}).execute()