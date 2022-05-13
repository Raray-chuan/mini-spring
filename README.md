# MySpringMVC
手写springMVC（准备实现类似spring boot的功能）

### 文档还没有补充，代码还在完善,等代码写完了会补充文档

##had finish
ioc:三级缓存加载bean;支持依赖注入
aop:支持类、包、方法上的切面; cglib实现aop
web:支持静态资源返回与text的返回
web:内嵌tomcat(类似springboot)

目前支持的注解有：@Autowired、@Aspect、@PointCut、@After、@Before、@ComponentScan、
                @Scope、@Controller、@Service、@Repository、@Component、@RequestMapping
                
 ##TODO 
 对Controller中参数的不同，进行不同的处理，如Map、String、List
 对以下注解的支持：@RequestBody 、@RequestParam、@ResponseBody
 对jdbc以及@Transaction的支持
 将所有功能实现，并开发一个小的web项目

