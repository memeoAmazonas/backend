package app.service;

import javax.ws.rs.*;

import app.recipe.Recipe;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@Path("recipe")
@Component
@Consumes("application/json")
@Produces("application/json")
public interface RecipeService {

	@GET
	@Path("/all")
	public RecipesResponse getRecipes();

	@POST
	@Path("/add")
	public ResponseEntity addRecipe(Recipe recipe) throws Exception;


	@POST
	@Path("/update")
	public  ResponseEntity updateRecipe(@RequestBody Recipe recipe) throws Exception;

	@GET
	@Path("/ingredients")
	public ResponseEntity getIngredients();

}
