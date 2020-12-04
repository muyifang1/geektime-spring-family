# 认识 Spring MVC #
- DispatcherServlet
  - Controller
  - XXXResolver
    - ViewResolver
    - HandlerExceptionResolver
    - MultipartResolver
  - HandlerMapping 请求映射到对应Controller 处理逻辑

## Spring MVC 中常用的注解 ##
`@Controller`
`@RestController` 结合了Controller和ResponseBody
`@RequestMapping` 定义Controller要处理哪些请求；可以指定url path;或者指定 http method
`@GetMapping` / `@PostMapping`
`@PutMapping` / `@DeleteMapping`
`@ResquestBody` / `@ResponseBody` / `@ResponseStatus` 对应请求和响应的报文体，可以通过ResponseStatus指定返回响应码
