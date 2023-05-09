# Part 1 (APIs (Java and Postman)):

The School of Computing and Mathematical Sciences has hired you to develop a system to organise and manage modules' timetables. They are interested in keeping track of convenors, the modules they teach, and the sessions delivered in each module. The data they are interested in keeping track of is as follows:

- **Convenors**
  - `long id`: unique identifier for a convenor
  - `String name`: the lecturer's name, e.g., "Kehinde Aruleba"
  - `Position position`: either "GTA," "Lecturer," or "Professor" (Position is an enum)
- **Modules**
  - `String code`: the module's code and unique identifier, e.g., "CO2103", "CO3095"
  - `String title`: the module's title, e.g., "Software Architecture", "Calculus I"
  - `int level`: the year in which the module is taught (a value between 1 and 4 representing Year 1–4)
  - `boolean optional`: indicates whether the module is optional (i.e., true) or mandatory (false).
- **Sessions**
  - `long id`: unique identifier for a session
  - `String topic`: the topic to be covered in the session, e.g., "RESTful APIs"
  - `Timestamp datetime`: indicates the date and time when the session is taking place
  - `int duration`: the duration of the session in minutes

Relationships between these resources:

- A module is taught by one or multiple convenors
- A convenor can teach many modules
- A module can have many sessions
- A session belongs to one module
- Deleting a convenor should delete all the modules they teach, except the modules that are also taught by another convenor, which should not be deleted
- Deleting a module should delete all the sessions in the module but not the convenor
- Deleting a session should not have any effect on modules or convenors

The API you design and implement should provide the following endpoints:

- Rest controller named `edu.leicester.co2103.controller.ConvenorRestController` with request mapping `/convenors`
  - List all convenors (i.e., GET /convenors) (endpoint #1):
  - Create (POST), retrieve (GET), update (only PUT) and delete (DELETE) a specific convenor (endpoints #2-5)
  - List all modules taught by a convenor: `/convenors/{id}/modules` (endpoint #6)
- Rest controller named `edu.leicester.co2103.controller.ModuleRestController` with request mapping `/modules`
  - List all modules (endpoint #7)
  - Create (POST), retrieve (GET), update (only PATCH) and delete (DELETE) a module (endpoints #8-11)
  - GET `/modules/{code}/sessions`: list all sessions in a module (endpoint #12)
  - Create (POST), retrieve (GET), update (PUT and PATCH) and delete (DELETE) a session in a module (e.g., POST `/modules/{code}/sessions` and GET `/modules/{code}/sessions/{id}`) (endpoints #13-17)
- Rest controller named `edu.leicester.co2103.controller.SessionRestController` with request mapping `/sessions`
  - Delete all sessions (DELETE /sessions)(endpoint #18)
  - List all sessions, allowing filtering by convenor (convenor ID) and by module (module code), e.g., `/sessions?convenor=1&module=co2103`, `/sessions?convenor=1` or `/sessions?module=co2103`. It should be possible to use only one filter or both together. (endpoints #19-20)

Your task is to implement all the endpoints described in the previous section in the appropriate rest controllers: `ConvenorRestController`, `ModuleRestController`, and `SessionRestController`. These files already exist in the project, but you have to write the necessary code to handle all the required REST requests.

In this task, you will use Postman to create two test requests for each endpoint: one for a successful response and one for an error response. Create a Postman Collection and ensure all the tests run correctly in sequence.

# Part 2 (Android):

The features that your app needs to provide are the following:

1. **List shopping lists on the main activity named MainActivity.** When the app starts, the existing shopping lists should be displayed on the main activity in a RecyclerView. Each recycler view item should include the shopping list image, the shopping list name. This main activity would be initially empty when the app is launched, and no shopping lists have been created yet. [15 marks]
2. **Create a shopping list using a second activity named CreateListActivity.** There should be a FloatingActionButton view with id `fab` on the main activity that leads the user to a second activity named `CreateListActivity`, which contains a simple form to input the details for a shopping list: a name and an optional image (to be loaded from the phone's gallery). The name is essential for creating a shopping list; the image is optional. The form contains a button with the text "Create" to create the shopping list and navigate back to the main activity. [15 marks]
3. **View shopping list.** When the user taps on a shopping list in the main activity, the app should navigate to a third activity named `ShoppingListActivity`. This activity should declare MainActivity as its parent. The activity will contain a RecyclerView, and each item in the recycler view will contain the details of a product in the shopping list: name, quantity, and unit. Each item in your shopping list should have a toast message showing a brief description of the item. [15 marks]
4. **Delete shopping list.** When the user performs a long-click (long-press) on a specific shopping list, an option is given to "Delete" the shopping list; tapping on the option should delete the shopping list with cascade-deletion of all the products it contains. [15 marks]
5. **Add product to shopping list using a fourth activity named AddProductActivity.** In `ShoppingListActivity` (the activity that displays a shopping list), add a FloatingActionButton with id `fabAddProduct`, which will navigate to `AddProductActivity`. In `AddProductActivity`, you will have two EditText views (one with id `editTextName` and input type text for the name of the product, and another one with id `editTextQuantity` and input type number for the quantity) and one Spinner view with id `spinner` that will allow you to choose between "Unit", "Kg", and "Liter". A button with text "Add" will save the product in the shopping list and navigate back to the product's `ShoppingListActivity`, where the product should have been added, unless a product with the same name already exists in the list, in which case a Toast must be displayed with the text "Product already exists". [15 marks]
6. **Edit or delete a product.** Tapping on a product in a shopping list should display a dialog with the options to "Edit" or "Delete" the selected product.

