package com.mainpackage;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class ManageStudent {
	private static SessionFactory factory;

	public static void main(String[] args) {

		try {

			factory = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		ManageStudent ME = new ManageStudent();

		Integer stu1 = ME.addStudent("Mahmut", "Tuncer", 43); // Ekleme işlemi yaptıgımız fonksiyonumuz
		Integer stu2 = ME.addStudent("Yıldız", "Tilbe", 23);
		Integer stu3 = ME.addStudent("Sezen", "Aksu", 63); // 3 öğrenci ekledik

		ME.listStudents(); // Db'den listeyi fetchledik ekranda gösterdik

		ME.updateStudent(stu1, 52); // Örnek teşkil etmesi açısından bir öğrencinin bilgilerini güncelledik

		ME.deleteStudent(stu2); // Ve bir öğrenciyi db'den sildik

		ME.listStudents(); // tekrar listeledik
	}

	public Integer addStudent(String fname, String lname, int age) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer ID = null;

		try {
			tx = session.beginTransaction();
			Student stu = new Student();
			stu.setFirst_name(fname);
			stu.setLast_name(lname);
			stu.setAge(age);
			ID = (Integer) session.save(stu);
			tx.commit(); // Gelen bilgileri nesnemizde setledikten sonra db'ye commitledik
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ID;
	}

	public void listStudents() {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			List students = session.createQuery("FROM Student").list();
			for (Iterator iterator = students.iterator(); iterator.hasNext();) {
				Student stu = (Student) iterator.next();
				System.out.print("First Name: " + stu.getFirst_name());
				System.out.print("  Last Name: " + stu.getLast_name());
				System.out.println("  Age: " + stu.getAge());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void updateStudent(Integer ID, int age) {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Student stu = (Student) session.get(Student.class, ID);
			stu.setAge(age);
			session.update(stu);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void deleteStudent(Integer ID) {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Student stu = (Student) session.get(Student.class, ID);
			session.delete(stu);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}