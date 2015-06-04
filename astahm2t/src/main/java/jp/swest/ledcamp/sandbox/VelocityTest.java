package jp.swest.ledcamp.sandbox;

import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class VelocityTest {
	public static void main(String[] args) {
		
		VelocityContext ctx = new VelocityContext();
		ArrayList<Method> methods = new ArrayList<Method>();
		methods.add(new Method("Hello"));
		methods.add(new Method("World"));
		methods.add(new Method("Foo"));
		ctx.put("methods", methods);
		ctx.put("include", "mylib.h");
		
		Template template = Velocity.getTemplate("template/sample.vm");
		StringWriter writer = new StringWriter();
		template.merge(ctx, writer);
		System.out.println(writer.toString());
	}
}

