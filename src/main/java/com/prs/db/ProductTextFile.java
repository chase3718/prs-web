package com.prs.db;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import com.prs.business.Product;
import com.prs.business.Vendor;

public class ProductTextFile {
	private List<Product> products = null;
	private Path productsPath = null;
	private File productsFile = null;
	private final static String FIELD_SEP = ",";

	public ProductTextFile() {
		productsPath = Paths.get("products.csv");
		productsFile = productsPath.toFile();
	}
	
	public ProductTextFile(String file) {
		productsPath = Paths.get(file);
		productsFile = productsPath.toFile();
	}

	public List<Product> getAll(Vendor v) {
		if (products != null) {
			return products;
		}

		products = new ArrayList<>();
		if (Files.exists(productsPath)) {
			try (BufferedReader in = new BufferedReader(new FileReader(productsFile))) {
				String line = in.readLine();
				while (line != null) {
					List<String> fields = new ArrayList<>(5);
					String[] s = line.split(FIELD_SEP);
					for (int i = 0; i < s.length; i++) {
						fields.add(s[i]);
					}
					if (fields.size() < 5) {
						fields.add(null);
						fields.add(null);
					}
					String partNumber = fields.get(0);
					partNumber.trim();
					String name = fields.get(1);
					name.trim();
					double price = Double.parseDouble(fields.get(2));
					String unit = fields.get(3);
					if (unit != null) {
						unit.trim();
						if (unit.equals("")) {
							unit = null;
						}
					}
					String photoPath = fields.get(4);
					if (photoPath != null) {
						photoPath.trim();
						if (photoPath.equals("")) {
							photoPath = null;
						}
					}
					Product p = new Product(v, partNumber, name, price, unit, photoPath);
					products.add(p);

					line = in.readLine();
				}
			} catch (IOException e) {
				System.out.println(e);
				return null;
			}
		} else {
			System.out.println(productsPath.toAbsolutePath() + " doesn't exist.");
			return null;
		}
		return products;
	}

}
