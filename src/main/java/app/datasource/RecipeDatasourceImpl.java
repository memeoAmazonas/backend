package app.datasource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import app.recipe.Ingredient;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import app.recipe.Recipe;

@Repository
@Transactional
public class RecipeDatasourceImpl  implements RecipeDatasource {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Recipe> findAll() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Recipe.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Recipe>)criteria.list();
	}

	@Override
	public boolean addRecipe(Recipe recipe) {
		boolean response;
		int idRecipe =  getActualId("recipe");
		Session session =  sessionFactory.openSession();
		Query query = session.createSQLQuery("INSERT INTO recipe (id,description, image_path, name) VALUES (:id, :description, :image_path, :name)");
		query.setParameter("id",idRecipe);
		query.setParameter("name", recipe.getName());
		query.setParameter("image_path", recipe.getImagePath());
		query.setParameter("description", recipe.getDescription());
		try {
			query.executeUpdate();
			response = true;
		}catch (Exception e){
			response = false;
		}
		if (response){
			for (Ingredient ingredient : recipe.getIngredients()){
				ingredient.setRecipeId(idRecipe);
				if(!addIngredient(ingredient)){
					response = false;
					break;
				}
			}
		}
		return response;
	}

	public int getActualId(String type) {
		Session session =  sessionFactory.openSession();
		Query query = session.createSQLQuery("select max(id) from "+ type);
		List<BigInteger> id = (List<BigInteger>)query.list();
		return  id.get(0).intValue()+1;
	}

	public boolean addIngredient(Ingredient ingredient){

		Session session =  sessionFactory.openSession();
		Query query  = session.createSQLQuery("INSERT INTO ingredient (id,amount,name,recipe_id) VALUES (:id, :amount, :name, :recipe_id)");
		query.setParameter("id", getActualId("ingredient"));
		query.setParameter("amount", ingredient.getAmount());
		query.setParameter("name",ingredient.getName());
		query.setParameter("recipe_id",ingredient.getRecipeId());
		try {
			query.executeUpdate();

			return  true;
		}catch (Exception e){
			return  false;
		}
	}

	@Override
	public boolean updateRecipe(Recipe recipe) {
		boolean response;
		Session session =  sessionFactory.openSession();
		Query query = session.createSQLQuery("UPDATE recipe SET description = :description, name = :name, image_path = :image_path where id = "+recipe.getId());
		query.setParameter("name", recipe.getName());
		query.setParameter("image_path", recipe.getImagePath());
		query.setParameter("description", recipe.getDescription());
		try {
			query.executeUpdate();
			response = true;
			if (deleteIngredients(recipe.getId())){
				for (Ingredient ingredient : recipe.getIngredients()){
					ingredient.setRecipeId(recipe.getId());
					if(!addIngredient(ingredient)){
						response = false;
						break;
					}
				}
			}else{
				session.cancelQuery();
				response = false;
			}
		}catch (Exception e){
			response = false;
		}
		return  response;
	}

	public boolean deleteIngredients (Long id) {
		Session session =  sessionFactory.openSession();
		Query query = session.createSQLQuery("DELETE from ingredient where recipe_id = "+id);
		try {
			query.executeUpdate();
			return  true;
		}catch (Exception e){
			return false;
		}
	}

	@Override
	public List<Ingredient> getIngredients() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Ingredient.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Ingredient> temps = (List<Ingredient>)criteria.list();
		List<String> names = new ArrayList<>();
		HashMap<String,Ingredient> result= new HashMap<>();
		for (Ingredient ingredient: temps){
			names.add(ingredient.getName());
		}
		for (Ingredient ingredient: temps){
			int total = 0;
			for (String name: names){
				if (name.equals(ingredient.getName())){
					total += ingredient.getAmount();
				}
			}
			Ingredient send = new Ingredient();
			send.setAmount(total);
			send.setName(ingredient.getName());
			if (!result.containsKey(ingredient.getName())){
				result.put(ingredient.getName(), send);
			}
		}
		temps.clear();
		result.forEach((k,v)->temps.add(v));

		return temps;
	}
}

