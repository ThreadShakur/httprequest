**httprequest**

Маленькая библиотека для HTTP запросов в JAVA

Главный класс: Request
Доступные методы: HttpURLConnection get(String url), HttpURLConnection post(String url)

**Перед вызовом метода get или post можно установить некоторые параметры запроса.**
**В объекте класса Request доступны следующие переменные - настройки:**

* CookieManager cookies - Массив с куками
* Map<String, String> params - GET параметры в GET запросе и POST form/urlencoded параметры в POST запросе
* Map<String, String> headers - Заголовки запроса
* String text - Переменная, в которую записывается тело ответа
* int connectTimeout - Тайм-аут подключения
* int readTimeout - Тайм-аут чтения тела страницы
* boolean storeCookies - Параметр, отвечающий за сохраниние cookies последнего запроса из заголовка Set-Cookies
* boolean followRedirects - Автоперенаправление со страниц

**Пример запроса:**

		Request req = new Request();
		
		req.cookies.getCookieStore().add(null, new HttpCookie("key", "value"));
		req.params.put("key", "value");
		req.headers.put("key", "value");
		
		
		req.get("https://httpbin.org/get");
		System.out.println(req.text);
