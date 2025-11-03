
//Assign API URL to its value to pull the necessary data from the backend
const apiUrl = "http://localhost:8080/api/items";

//Pull in elements using the DOM and assign them in JavaScript
const listEl = document.getElementById("list");
const form = document.getElementById("addForm");
const titleInput = document.getElementById("title");
const descriptionInput = document.getElementById("description");

//Creates an asynchronous function to fetch items from the backend. Using async allows the use of
//await to pause execution while waiting for the network request.
async function fetchItems() {
    try {
        //Make an HTTP GET request to the URL stored, store the HTTP response object
         //from the server, and pause until the request is complete
        const response = await fetch(apiUrl);

        //Read the response body and parse it as JSON. Await requires the parsing to finish first
        //and items will now store the data
        const items = await response.json();

        //Clear any existing content before rendering the new list
        listEl.innerHTML = "";

        //Loop over each element in the items array and call the renderItem function to create
        //HTML for that item and add it to the page
        items.forEach(item => renderItem(item));
    } catch (error) {
        //If the request failed, catch the error and provide it.
        console.error("Failed to fetch items:", error);
    }
}

//Create a function to render the items.
function renderItem(item) {
    //Dynamically create a new <li> element in memory, stored using li
    const li = document.createElement("li");

    /*Assign styling using bootstrap:
    - list-group-item - makes the <li> part of a Bootstrap list group, giving it consistent padding, borders, and hover effects
    - d-flex - applies the flexbox layout to the <li>
    - flex-column - on small screens, arrange children vertically (stacked)
    - flex-md-row - on medium+ screens, arrange children horizontally (side by side)
    - align-items-start - vertically aligns items at the start on small screens
    - align-items-md-center - vertically centers items on medium+ screens
    - justify-content-between - places children at opposite ends, creating spacing between them
    */
    li.className = "list-group-item d-flex flex-column flex-md-row align-items-start align-items-md-center justify-content-between";

    //Dynamically create a new <div> element in memory, stored using textContainer
    const textContainer = document.createElement("div");

    /*Assign styling using bootstrap
    - flex-grow-1 - makes the elemnet expand to take up the remaining space
    -mb-2 - adds a margin bottom of bootstrap spacing (0.5rem x 2 = 1 rem)
    -mb-md-0 - on medium+ screens, the margin is removed
    */
    textContainer.className = "flex-grow-1 mb-2 mb-md-0";

    //Create a new HTML element <strong> to emphasize importance. The element is stored
    //in the variable titleEl in memory.
    const titleEl = document.createElement("strong");

    //set the text content of the <strong> element to the title property of the item
    titleEl.textContent = item.title;

    //Add a css class called title to the element
    titleEl.classList.add("title");

    //if the item is completed, add a completed class to the element
    if (item.completed) titleEl.classList.add("completed");

    //Create a new HTML element, <span>, in memory
    const descEl = document.createElement("span");

    //Set the text content inside the span to the item description or leave blank if null, undefined, or empty
    descEl.textContent = item.description || "";

    //Add the CSS class "description" to the <span>
    descEl.classList.add("description");

    //Add the titleEl (<strong>) as a child of textContainer, so titleEl now appears inside the textContainer element
    textContainer.appendChild(titleEl);

    //Create a new <br> element and append it to textContainer
    //This forces a line break between the title and description
    textContainer.appendChild(document.createElement("br"));

    //Adds the description element (descEl, the <span> with the description text) after the <br>
    textContainer.appendChild(descEl);

    //Create a new <div> element and store it in btnGroup
    const btnGroup = document.createElement("div");

    //Add the bootstrap class btn-group, which groups buttons together horizontally in a nice layout
    btnGroup.className = "btn-group";

    //Create a new <button> element and store it in toggleBtn
    const toggleBtn = document.createElement("button");

    /* Add multiple CSS classes:
    - btn - basic Bootstrap button styling
    - btn-toggle - custom class
    - btn-sm - small button
    - toggle-complete - custom class
    */
    toggleBtn.className = "btn btn-toggle btn-sm toggle-complete";

    //Set button text depending on whether the item is completed.
    toggleBtn.textContent = item.completed ? "Undo" : "Done";

    //Create a button for editing
    const editBtn = document.createElement("button");

    /*Add classes to handle information:
    - btn - Bootstrap button
    - btn-edit - custom styling for edit button
    - btn-sm - small button
    - edit - custom class*/
    editBtn.className = "btn btn-edit btn-sm edit";

    //Update text content to edit
    editBtn.textContent = "Edit";

    //Create a button element stored in deleteBtn
    const deleteBtn = document.createElement("button");

    /*Add multiple CSS classes to style the button
    - btn - basic Bootstrap button styling
    - btn-delete - custom class for delete-specific styling
    - btn-sm - small-sized button
    - delete - custom class*/
    deleteBtn.className = "btn btn-delete btn-sm delete";

    //Update text content to delete
    deleteBtn.textContent = "Delete";

    //Add three buttons (toggleBtn, editBtn, deleteBtn) as children of btnGroup
    btnGroup.appendChild(toggleBtn);
    btnGroup.appendChild(editBtn);
    btnGroup.appendChild(deleteBtn);

    //Add the text container (textContainer with title and description) and the btnGroup (with buttons)
    //inside the list item <li>
    li.appendChild(textContainer);
    li.appendChild(btnGroup);

    //Add the fully constructed <li> into the main list element, listEl
    listEl.appendChild(li);

    //Create an event listener to run the function inside upon click. The function
    //is marked as async because it uses await for asynchronous operations like fetch.
    toggleBtn.addEventListener("click", async () => {
        //Create a new object called updated and use the spread operator (...) to copy all properties from item.
        //Then overwrite the completed property to the opposite of its current value.
        const updated = { ...item, completed: !item.completed };

        //Make an HTTP PUT request to update the item on the server
        await fetch(`${apiUrl}/${item.id}`, { //Tells the backend what to update. Await pauses execution until the server responds.
            method: "PUT", //Tells the backend that this is an update
            headers: { "Content-Type": "application/json" }, //This tells the server to expect JSON in the body
            body: JSON.stringify(updated) //This converts the updated object to a JSON string to send to the server
        });
        //Calls the fetchItems() function again to reload the list from the server
        fetchItems();
    });

    //Add an event listen to run a function when the edit button is clicked
    editBtn.addEventListener("click", () => {
        // Create label and input for title
        const titleLabel = document.createElement("label");
        titleLabel.textContent = "Title"; //Set the text content to label
        titleLabel.style.fontWeight = "bold"; //set the font-weight to bold

        //create an <input> element stored in titleInput
        const titleInput = document.createElement("input");

        //Set the type of input to text
        titleInput.type = "text";

        //Set the value of the titleInput to the title
        titleInput.value = item.title;

        //Ensures it’s on a new line by using block
        titleInput.style.display = "block";

        //Set full width
        titleInput.style.width = "100%";

        // Create label and input for description
        const descLabel = document.createElement("label");

        //Set the text content to "Description"
        descLabel.textContent = "Description";

        //Set the style to bold
        descLabel.style.fontWeight = "bold";

        //Create a new <input> element stored in descInput
        const descInput = document.createElement("input");

        //Set the type equal to text
        descInput.type = "text";

        //Set the value of descInput to the description or blank if null, empty, etc.
        descInput.value = item.description || "";

        //Ensure that it's on a new line by using block
        descInput.style.display = "block";

        //Set width to 100%
        descInput.style.width = "100%";

        // Clear previous content in inner HTML
        textContainer.innerHTML = "";

        //Add new elements back, in order
        textContainer.appendChild(titleLabel);
        textContainer.appendChild(titleInput);
        textContainer.appendChild(descLabel);
        textContainer.appendChild(descInput);

        // Create save button stored in saveBtn
        const saveBtn = document.createElement("button");

        //Make the text content "Save"
        saveBtn.textContent = "Save";

        /*Add Bootstrap classes to style the button
        - btn - make it a Bootstrap-styled button
        - btn-sml - make it a small button
        - btn-primary - give it the primary color
        - mt-2 - adds a small margin top (for spacing above the button)*/
        saveBtn.className = "btn btn-sm btn-primary mt-2";

        //Create an event listener so that when the user clicks the "Save" button, the code inside runs
        saveBtn.addEventListener("click", async () => {
        //The ...item copies all the original properties of the item object and overwrites the title and description with whatever
        //values the user entered in the input field. The result is a new object (updated) that's identical to the original item
        //except for the updated text.
            const updated = {
                ...item,
                title: titleInput.value,
                description: descInput.value
            };

            //Send an HTTP PUT request to the backend API to update the item in the databse
            await fetch(`${apiUrl}/${item.id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" }, //Send JSON data
                body: JSON.stringify(updated) //Convert the object into a JSON string
            });

            //Upon completion, reload the list of items from the server so the user interface shows the new values
            fetchItems();
        });

        //Add the "Save" button into the page's HTML inside the textContainer where the title and description input fields are
        textContainer.appendChild(saveBtn);
    });

    //If the delete button is clicked, show a confirmation pop-up. If the user clicks ok, proceed to delete. Otherwise, return false and do nothing
    deleteBtn.addEventListener("click", async () => {
        if (confirm("Delete this item?")) {
            //Make an HTTP request to the backend endpoint for that specific item. The await ensures that the code waits for the request to finish before continuing.
            await fetch(`${apiUrl}/${item.id}`, { method: "DELETE" });

            //Re-fetch the updated list from the backend so that the removed items disappear from the page.
            fetchItems();
        }
    });
}

//Add an event listener if the user hits submit for a new item
form.addEventListener("submit", async (e) => {
    //Prevents the page from default reload
    e.preventDefault();

    //Build a new item object with the user's title and description. Set completed to false initially.
    const newItem = {
        title: titleInput.value,
        description: descriptionInput.value,
        completed: false
    };

    //Submit data to the backend
    await fetch(apiUrl, {
        method: "POST",

        //Tell the server that the request is in JSON format
        headers: { "Content-Type": "application/json" },

        //Convert the object into a JSON string for the backend
        body: JSON.stringify(newItem)
    });

    //Reset the title and description values so they are empty again, then reload the list to show the new item
    titleInput.value = "";
    descriptionInput.value = "";
    fetchItems();
});

// Initial load
fetchItems();
