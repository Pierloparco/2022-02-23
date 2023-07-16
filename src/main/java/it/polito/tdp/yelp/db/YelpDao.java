package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.yelp.model.Adiacenza;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {
	
	
	public List<Business> getAllBusiness(){
		String sql = "SELECT * FROM Business";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getAllReviews(Map<String, Review> idMap){
		String sql = "SELECT * FROM Reviews";
//		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getString("review_id"))) {
					Review review = new Review(res.getString("review_id"), 
							res.getString("business_id"),
							res.getString("user_id"),
							res.getDouble("stars"),
							res.getDate("review_date").toLocalDate(),
							res.getInt("votes_funny"),
							res.getInt("votes_useful"),
							res.getInt("votes_cool"),
							res.getString("review_text"));
					idMap.put(review.getReviewId(), review);
				}
	//			result.add(review);
			}
			res.close();
			st.close();
			conn.close();
//			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
//			return null;
		}
	}
	
	public List<User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.add(user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getAllCities() {
		String sql = "SELECT DISTINCT b.`city` "
				+ "FROM `Business` b "
				+ "ORDER BY b.`city` ASC ";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				result.add(res.getString("city"));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Business> getLocaliCitta(String city) {
		String sql = "SELECT DISTINCT b.* "
				+ "FROM `Business` b "
				+ "WHERE b.`city` = ? "
				+ "GROUP BY b.`business_id`";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, city);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Review> getVertici(Business b, Map<String, Review> idMap) {
		String sql = "SELECT DISTINCT r.* "
				+ "FROM `Reviews` r "
				+ "WHERE r.`business_id` = ? "
				+ "GROUP BY r.`review_id`";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, b.getBusinessId());
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				result.add(idMap.get(res.getString("review_id")));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			return null;
		}
	}
	
	
	public List<Adiacenza> getArchi(Business b, Map<String, Review> idMap) {
		String sql = "SELECT r1.`review_id` AS v1, r2.`review_id` AS v2, r1.`review_date` AS d1, r2.`review_date` AS d2 "
				+ "FROM `Reviews` r1, `Reviews` r2, `Business` b1, `Business` b2 "
				+ "WHERE r1.`business_id`=b1.`business_id` AND r2.`business_id`=b2.`business_id` AND b1.`business_id`=?  AND b1.`business_id`=b2.`business_id` AND r1.`review_id`<>r2.`review_id` AND r2.`review_date`>r1.`review_date` "
				+ "GROUP BY r1.`review_id`, r2.`review_id` ";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, b.getBusinessId());
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Double peso = (double) ChronoUnit.DAYS.between(res.getDate("d1").toLocalDate(), res.getDate("d2").toLocalDate());
				if(peso > 0.0) {
					result.add(new Adiacenza(idMap.get(res.getString("v1")), idMap.get(res.getString("v2")), peso));
				}	
				
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			return null;
		}
	}
		
	
}
