package demo.me.test;

import demo.me.base.DaoFactory;


public class Test {
	public static void main(String[] args) throws Exception {
		System.out.println(DaoFactory.getInstance().getUserDao().findAll());
	}
}
