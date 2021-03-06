package aop.principle;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * JDK动态代理：依赖接口实现，
 * 优点：可以突破静态代理的限制，一个动态代理类为多个接口服务
 * 缺点：无法用于未实现接口的目标类
 * AOP的原理：JDK动态代理和cglib动态代理
 */
public class ArithmeticCalculatorLoggingProxy {
	//要代理的对象
	ArithmeticCalculator target;
	
	public ArithmeticCalculatorLoggingProxy(ArithmeticCalculator target){
		this.target=target;
	}
	
	public ArithmeticCalculator getLoggingProxy(){
		ArithmeticCalculator proxy=null;
		
		//代理对象由哪一个类加载器负责加载
		ClassLoader loader=target.getClass().getClassLoader();
		//代理对象的类型，即告知其中有哪些方法
		//Class[] interfaces=new Class[]{ArithmeticCalculator.class};
		//更简洁通用的写法
		Class[] interfaces=target.getClass().getInterfaces();
		//当调用代理对象其中的方法时，该执行的代码
		InvocationHandler h=new InvocationHandler(){
			/**
			 * proxy:正在返回的代理对象。不能在invoke中使用该对象，否则将循环调用
			 * method:目标对象被调用的方法
			 * args:调用方法时，传入的参数
			 */
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//				System.out.println("invoke...");
//				return 0;
				String methodName=method.getName();
				//日志
				System.out.println("The method "+methodName+" begins with "+Arrays.asList(args));
				//目录对象执行方法
				Object result=method.invoke(target, args);
				//日志
				System.out.println("The method "+methodName+" ends with "+Arrays.asList(args));
				return result;
			}
			
		};
		
		proxy=(ArithmeticCalculator)Proxy.newProxyInstance(loader, interfaces, h);
		return proxy;
	}
}
