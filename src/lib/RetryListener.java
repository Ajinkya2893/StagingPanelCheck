		package lib;
	
	import java.lang.reflect.Constructor;
	import java.lang.reflect.Method;
	import org.testng.IAnnotationTransformer;
	import org.testng.IRetryAnalyzer;
	import org.testng.annotations.ITestAnnotation;

	import lib.Retry;
	
	public class RetryListener implements IAnnotationTransformer {
		
	    public void transform(ITestAnnotation testannotation, @SuppressWarnings("rawtypes") Class testClass, @SuppressWarnings("rawtypes") Constructor testConstructor, Method testMethod) {
	        IRetryAnalyzer retry = testannotation.getRetryAnalyzer();
	        	if (retry == null) 
	        		{
	        			testannotation.setRetryAnalyzer(Retry.class);	      
	        		}
	        	
	    }	
	}

