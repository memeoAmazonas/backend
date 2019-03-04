package app.service;

import app.recipe.Ingredient;
import app.recipe.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import app.datasource.RecipeDatasource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

public class RecipeServiceImpl implements RecipeService {

	private static final Logger logger = LoggerFactory.getLogger(RecipeServiceImpl.class);

	@Autowired
	private RecipeDatasource datasource;

	@Override
	public RecipesResponse getRecipes() {

		RecipesResponse response = new RecipesResponse();
		response.setCode("200");
		response.setDescription("ok");

		try{
			response.getRecipes().addAll(datasource.findAll());
		} catch (Exception e) {
			logger.error("Error al obtener las recetas de la base de datos.");
			response.setCode("500");
			response.setDescription("Ha ocurrido un error al consultar las recetas");
		}

		return response;
	}

	@Override
	public ResponseEntity addRecipe(@RequestBody  Recipe recipe) throws Exception {
		ResponseEntity response = null;
		boolean result = false;
		try {
			if (datasource.addRecipe(recipe))
				response =  new ResponseEntity(true, HttpStatus.OK);
			else
				response = new ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e){
			logger.error("Error al insertar la receta en la base de datos.");
		}
		return response;
	}

	@Override
	public ResponseEntity updateRecipe(@RequestBody Recipe recipe) throws Exception {
		ResponseEntity response = null;
		try {
			if (datasource.updateRecipe(recipe))
				response =  new ResponseEntity(true, HttpStatus.OK);
			else
				response = new ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e){
			logger.error("Error al insertar la receta en la base de datos.");
		}
		return response;
	}

	@Override
	public ResponseEntity getIngredients() {
		ResponseEntity response;
		try {
			List<Ingredient> ingredients =  datasource.getIngredients();
			response =  new ResponseEntity(ingredients, HttpStatus.OK);
		}catch (Exception e){
			response =  new ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR);
			logger.error("Error al obtener los ingredientes de la base de datos.");
		}
		return  response;
	}
}

