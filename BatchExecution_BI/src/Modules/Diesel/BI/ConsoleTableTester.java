package Modules.Diesel.BI;

import java.util.ArrayList;

/*
 * Just a small test class that shows how the ConsoleTable class works
 */

public class ConsoleTableTester{

	public ConsoleTableTester(){
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("Row");
		headers.add("Surname");
		headers.add("Lastname");
		headers.add("Age");

		ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
		ArrayList<String> row1 = new ArrayList<String>();
			row1.add("1");
			row1.add("DonaldDonaldDonaldDonaldDonaldDonaldDonald");
			row1.add("DonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonaldDonald");
			row1.add("55");
		ArrayList<String> row2 = new ArrayList<String>();
			row2.add("2");
			row2.add("Huey");
			row2.add("Duck");
			row2.add("13");
		ArrayList<String> row3 = new ArrayList<String>();
			row3.add("3");
			row3.add("Dewey");
			row3.add("Duck");
			row3.add("13");
		ArrayList<String> row4 = new ArrayList<String>();
			row4.add("4");
			row4.add("Louie");
			row4.add("Duck");
			row4.add("13");
		content.add(row1);
		content.add(row2);
		content.add(row3);
		content.add(row4);

		ConsoleTable ct = new ConsoleTable(headers,content);
		ct.printTable();
	}

	public static void main(String[] args) {
		new ConsoleTableTester();
	}
}