# 手写spring之ioc

## 1.什么是IOC
   IOC Inversion of Control 即控制反转，是指程序将创建对象的控制权转交给Spring框架进行管理，由Spring通过java的反射机制根据配置文件在运行时动态的创建实例，并管理各个实例之间的依赖关系。

## 2.什么是三级缓存
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211151651233.png)
- 第一级缓存：也叫单例池，存放已经经历了完整生命周期的Bean对象。
- 第二级缓存：存放早期暴露出来的Bean对象，实例化以后，就把对象放到这个Map中。（Bean可能只经过实例化，属性还未填充）。
- 第三级缓存：存放早期暴露的Bean的工厂。
```
注：
只有单例的bean会通过三级缓存提前暴露来解决循环依赖的问题，而非单例的bean，每次从容器中获取都是一个新的对象，都会重新创建，所以非单例的bean是没有缓存的，不会将其放到三级缓存中。

为了解决第二级缓存中AOP生成新对象的问题，Spring就提前AOP，比如在加载b的流程中，如果发送了循环依赖，b依赖了a，就要对a执行AOP，提前获取增强以后的a对象，这样b对象依赖的a对象就是增强以后的a了。

二三级缓存就是为了解决循环依赖，且之所以是二三级缓存而不是二级缓存，主要是可以解决循环依赖对象需要提前被aop代理，以及如果没有循环依赖，早期的bean也不会真正暴露，不用提前执行代理过程，也不用重复执行代理过程。
```


## 3.手动实现的简单流程
- 创建@ComponentScan,@Component,@Controller,@Repository,@Service,@Autowired,@Scope等注解
- 创建三级缓存类
- 在初始化SpringContext的时候扫描基础包下的所有class,存储到一个集合中
- 将类上含有@Component,@Controller,@Repository,@Service注解的class,封装成BeanDefinition,然后存储到 Bean容器中
- 遍历Bean容器中的所有Bean,如果是单例对象,实例化bean并放在三级缓存中。如果该bean上有@Autowired注解,需要将@Autowired的类实例化放在三级缓存中




## 4.IOC相关的基础类

### 4.1 定义IOC相关的注解

#### 4.1.1 @ComponentScan
设置包根路径注解
```java
/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description 设置根路径的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ComponentScan {
    String value();
}
```
#### 4.1.2 @Component
```java
/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description Component通用注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    String value() default "";
}
```
#### 4.1.3 @Controller
```java
/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description Controller注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    String value() default "";
}
```
#### 4.1.4 @Repository
```java
/**
 * @Author Xichuan
 * @Date 2022/5/7 11:43
 * @Description Repository注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {
    String value() default "";
}
```
#### 4.1.5 @Service
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
    String value() default "";
}
```
#### 4.1.6 @Autowired
```java
@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    String value() default "";
}
```
#### 4.1.7 @Scope
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scope {
    String value();
}
```



### 4.2 BeanDefinition
在基础包下的每一个class在被SpringContext扫描到后，都会封装成BeanDefinition进行管理
```java
/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description Bean定义信息
 */
public class BeanDefinition {

    //Class
    private Class clazz;

    //Bean名词
    private String beanName;

    //是否是Controller注解
    private  boolean isController = false;

    //作用域1、singleton 2、prototype
    private String scope;

    //根据扩容原理，如果不添加元素，永远是空数组不占用空间
    private ArrayList<BeanPostProcessor> beanPostProcessors=new ArrayList<>();

    ...
    ...
}
```


### 4.3 BeanContainer
存放三级缓存的类
```java
/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description bean实例容器
 */
public class BeanContainer {
    //一级缓存，日常实际获取Bean的地方
    public static ConcurrentHashMap<String,Object> singletonObjects = new ConcurrentHashMap<>();
    //二级缓存，临时
    public static ConcurrentHashMap<String,Object> earlySingletonObjects = new ConcurrentHashMap<>();
    //三级缓存，value是一个动态代理对象工厂
    public static ConcurrentHashMap<String, DynamicBeanFactory> singletonFactory = new ConcurrentHashMap<>();
    //Controller对象容器
    public static ConcurrentHashMap<String,Object> controllerMap = new ConcurrentHashMap<>();
    //类加载器,在SpringContext的静态代码块中进行赋值的
    public static ClassLoader classLoader = null;
}
```



## 5.实现IOC的过程
### 5.1 SpringContext初始化过程
```java
/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */
public class SpringContext {

    static {
        ClassLoader classLoader = SpringContext.class.getClassLoader();//拿到应用类加载器
        //给容器类赋值类加载器
        BeanContainer.classLoader=classLoader;
    }

    /**
     * 通过@ComponentScan注解获取根路径，从而加载根路径下的所有class
     * @param config
     */
    public SpringContext(Class<?> config) {
        if(BeanContainer.singletonObjects.size()==0) {
            //获取根路径
            ComponentScan componentScanAnnotation = (ComponentScan) config.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();
            //获取packagePath下的所有class，注册到classesHashSet
            LoadBeanHelper.loadAllClass(path);
            //将BeanDefinition、BeforeDelegatedSet、AfterDelegatedSet、BeanPostProcessorList进行注册
            LoadBeanHelper.loadAllBeanDefinition();
            //生产bean,将需要代理的bean进行代理，放到一级缓存中
            LoadBeanHelper.productBean();
        }
    }

    /**
     * 通过config.properties配置文件，来加载根路径，从而加载根路径下的所有class
     */
    public SpringContext() {
        if(BeanContainer.singletonObjects.size()==0) {
            //获取packagePath下的所有class，注册到classesHashSet
            LoadBeanHelper.loadAllClass(ConfigHelper.getAppBasePackage());
            //将BeanDefinition、BeforeDelegatedSet、AfterDelegatedSet、BeanPostProcessorList进行注册
            LoadBeanHelper.loadAllBeanDefinition();
            //生产单例bean,将需要代理的bean进行代理，放到一级缓存中
            LoadBeanHelper.productBean();
        }

    }
}
```
我们先看这两个重载构造方法的不同：`SpringContext(Class<?> config)`与`SpringContext()`,
这两个重载方法的区别在于：

`SpringContext(Class<?> config)`构造方法是通过`@ComponentScan`注解来获取包的根路径
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211151824800.png)
`SpringContext()`构造方法是通过`config.properties`配置文件中的`scan.dir=com.xichuan.dev`配置来获取包的根路径
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211151822194.png)

我们可以看出`SpringContext`做了三件事:
- `loadAllClass()`:获取`packagePath`下的所有class,将所有扫描到的class存放到`classesHashSet`中
- `loadAllBeanDefinition()`:遍历`classesHashSet`中的所有class，如果该class上含有`@Component,@Controller,@Repository,@Service`注解，将该class封装成`BeanDefinition`,并存放到`beanDefinitionHashMap`中
- `productBean()`:核心方法,遍历`beanDefinitionHashMap`中的所有`BeanDefinition`,将符合条件的`BeanDefinition`存放到`BeanContainer.singletonObjects`一级缓存中

我们依次看这个三个方法的代码



### 5.2 LoadBeanHelper.loadAllClass
```java
    /**
     *获取packagePath下的所有class
     *
     * @param packagePath 扫描路径
     * @return 所有类的class对象
     */
    public static Set<Class<?>> loadAllClass(String packagePath) {
        basePackage = packagePath;
        List<String> classNames = PackageUtil.getClassName(packagePath);
        Class<?>clazz;
        for (String className:classNames) {
            try {
                clazz=classLoader.loadClass(className);
                classesHashSet.add(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classesHashSet;
    }
```
我们可以看到，此方法的代码非常简单，只是将`packagePath`包路径下的class扫描出来，并存放到`classesHashSet`中;因为代码比较简单，递归获取class的操作就不细说了。

有一点需要注意的是，因为在idea运行与达成jar运行,扫描包的方式不一样,具体代码看此方法:`com.xichuan.framework.core.helper.PackageUtil.getClassName(java.lang.String, boolean)`
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211151910741.png)



### 5.3 LoadBeanHelper.loadAllBeanDefinition
```java
    /**
     * 将所有的BeanDefinition放入map中，（只有@Component、@Controller、@Service、@Repository才会放入）
     */
    public static void loadAllBeanDefinition() {
        for (Class<?> clazz : classesHashSet) {
            if (!(clazz.isAnnotationPresent(Component.class)||
                    clazz.isAnnotationPresent(Controller.class)||
                    clazz.isAnnotationPresent(Service.class) ||
                    clazz.isAnnotationPresent(Repository.class)))
                continue;

            //创建BeanDefinition
            BeanDefinition newBeanDefine = new BeanDefinition();
            newBeanDefine.setClazz(clazz);
            //获取beanName
            String BeanName=getBeanNameByClass(clazz);

            //判断是否是Controller类
            if(clazz.isAnnotationPresent(Controller.class))
                newBeanDefine.setIsController(true);
            newBeanDefine.setBeanName(BeanName);

            //加载后置处理器
            resolveBeanPostProcessor(clazz);

            //scope作用域注解
            if (clazz.isAnnotationPresent(Scope.class)) {
                String scope = clazz.getDeclaredAnnotation(Scope.class).value();
                newBeanDefine.setScope(scope);
            } else {
                //默认为单例模式
                newBeanDefine.setScope(ScopeEnum.SingleTon.getName());
            }

            //对@Aspect切面类的处理
            resolveAspect(clazz);

            //将每一个beanDefinition放在map种
            beanDefinitionHashMap.put(newBeanDefine.getBeanName(), newBeanDefine);
        }
    }
```
我们可以看出，此方法也是很简单，
此方法遍历`classesHashSet`中的所有class，如果该class上含有`@Component,@Controller,@Repository,@Service`注解，将该class封装成`BeanDefinition`,并存放到`beanDefinitionHashMap`中

`BeanDefinition`的属性有:
- `clazz`:此bean的clazz
- `beanName`:此bean的名称
- `isController`:此bean是否还有`@Controller`注解
- `scope`:作用域,`singleton`或`prototype`

此方法还有对aop相关的处理，此处先不过多介绍，下一个文章会专门介绍aop相关的代码



### 5.4 LoadBeanHelper.productBean()
