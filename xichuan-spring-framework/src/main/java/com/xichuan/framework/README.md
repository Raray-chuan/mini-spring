<!--
  一些个人小结以及参考代码以及博客地址:
-->

<!--
# MySpringMVC
手写springMVC（实现类似spring boot的功能）

### 文档还没有补充，代码还在完善,等代码写完了会补充文档

##Had finish
ioc:三级缓存加载bean;支持依赖注入
aop:支持类、包、方法上的切面; cglib实现aop
web:支持静态资源返回与text的返回
web:内嵌tomcat(类似springboot)

目前支持的注解有：@Autowired、@Aspect、@PointCut、@After、@Before、@ComponentScan、
                @Scope、@Controller、@Service、@Repository、@Component、@RequestMapping
                @RequestBody 、@RequestParam、@ResponseBody
                
##TODO 不准备继续写了
SpringMVC的优化(对modelView的转发一些优化、GET/POST/PUT/DELETE...请求的分别处理)
RequestParam  ResponseBody  RequestBody 接受json定义的时候map或者是一个pojo类;对GET/POST/PUT/DELETE的支持

JDBC操作
@Transation注解的实现
@Configuration @Bean的注解的实现（可以往后拖一拖）

-->




<!--
## springboot启动流程
https://gitee.com/nsgxa/mini-spring


## @WebServlet   urlPatterns 匹配规则顺序；   /  与   /*的区别
https://blog.csdn.net/xing930408/article/details/106276630


## embed tomcat使用示例
https://github.com/hengyunabc/executable-embeded-tomcat-sample


##J DK动态代理与CGlib
JDK 动态代理有一个最致命的问题是其只能代理实现了接口的类


## JAVA8反射获取方法参数名
https://blog.csdn.net/ilovewqf/article/details/103198763

## BeanNameAware作用
https://blog.csdn.net/weixin_34221332/article/details/91931997
如果一个bean实现了BeanNameAware，那么这个bean可以自动获取beanName


## BeanPostProcessor作用(所有bean进行初始化前后都会调用这两个方法)
https://blog.csdn.net/qq_43185206/article/details/107787308
Spring容器中完成bean实例化、配置以及其他初始化方法前后要添加一些自己逻辑处理


## InitializingBean作用
https://blog.csdn.net/maclaren001/article/details/37039749
InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，凡是继承该接口的类，在初始化bean的时候会执行该方法。
-->



<!--
Spring三级缓存
https://www.cnblogs.com/tiancai/p/16091965.html

###2.1 Spring是怎么解决循环依赖的？ 如果现在有个A对象，它的属性是B对象，而B对象的属性也是A对象;说白了就是A依赖B，而B又依赖A，Spring是怎么做的？
它的大致过程是这样的：
首先A对象实例化，然后对属性进行注入，发现依赖B对象
B对象此时还没创建出来，所以转头去实例化B对象
B对象实例化之后，发现需要依赖A对象，那A对象已经实例化了嘛，所以B对象最终能完成创建
B对象返回到A对象的属性注入的方法上，A对象最终完成创建
上面就是大致的过程；

###2.2 三级缓存
singletonObjects（一级，日常实际获取Bean的地方）；
earlySingletonObjects（二级，还没进行属性注入，由三级缓存放进来）；     第二级缓存考虑性能
singletonFactories（三级，Value是一个对象工厂）；    第三级缓存考虑代理

###2.3 三级缓存过程
再回到刚才讲述的过程中，A对象实例化之后，属性注入之前，其实会把A对象放入三级缓存中
key是BeanName，Value是ObjectFactory
等到A对象属性注入时，发现依赖B，又去实例化B时
B属性注入需要去获取A对象，这里就是从三级缓存里拿出ObjectFactory，从ObjectFactory得到对应的Bean（就是对象A）
把三级缓存的A记录给干掉，然后放到二级缓存中
显然，二级缓存存储的key是BeanName，value就是Bean（这里的Bean还没做完属性注入相关的工作）
等到完全初始化之后，就会把二级缓存给remove掉，塞到一级缓存中
我们自己去getBean的时候，实际上拿到的是一级缓存的

###2.4 为什么是三级缓存？
首先从第三级缓存说起（就是key是BeanName，Value为ObjectFactory）
我们的对象是单例的，有可能A对象依赖的B对象是有AOP的（B对象需要代理）
假设没有第三级缓存，只有第二级缓存（Value存对象，而不是工厂对象）
那如果有AOP的情况下，岂不是在存入第二级缓存之前都需要先去做AOP代理？这不合适嘛
这里肯定是需要考虑代理的情况的，比如A对象是一个被AOP增量的对象，B依赖A时，得到的A肯定是代理对象的
所以，三级缓存的Value是ObjectFactory，可以从里边拿到代理对象
而二级缓存存在的必要就是为了性能，从三级缓存的工厂里创建出对象，再扔到二级缓存（这样就不用每次都要从工厂里拿）
-->

















