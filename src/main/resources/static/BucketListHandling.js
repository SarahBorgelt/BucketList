
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

    */
    textContainer.className = "flex-grow-1 mb-2 mb-md-0";

    const titleEl = document.createElement("strong");
    titleEl.textContent = item.title;
    titleEl.classList.add("title");
    if (item.completed) titleEl.classList.add("completed");

    const descEl = document.createElement("span");
    descEl.textContent = item.description || "";
    descEl.classList.add("description");

    textContainer.appendChild(titleEl);
    textContainer.appendChild(document.createElement("br"));
    textContainer.appendChild(descEl);

    // Button container
    const btnGroup = document.createElement("div");
    btnGroup.className = "btn-group";

    const toggleBtn = document.createElement("button");
    toggleBtn.className = "btn btn-toggle btn-sm toggle-complete";
    toggleBtn.textContent = item.completed ? "Undo" : "Done";

    const editBtn = document.createElement("button");
    editBtn.className = "btn btn-edit btn-sm edit";
    editBtn.textContent = "Edit";

    const deleteBtn = document.createElement("button");
    deleteBtn.className = "btn btn-delete btn-sm delete";
    deleteBtn.textContent = "Delete";

    btnGroup.appendChild(toggleBtn);
    btnGroup.appendChild(editBtn);
    btnGroup.appendChild(deleteBtn);

    li.appendChild(textContainer);
    li.appendChild(btnGroup);
    listEl.appendChild(li);

    // Event listeners

    // Toggle completed
    toggleBtn.addEventListener("click", async () => {
        const updated = { ...item, completed: !item.completed };
        await fetch(`${apiUrl}/${item.id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updated)
        });
        fetchItems();
    });

    // Edit item
    editBtn.addEventListener("click", () => {
        const newTitle = prompt("Update title:", item.title);
        const newDesc = prompt("Update description:", item.description);
        if (newTitle !== null) {
            const updated = { ...item, title: newTitle, description: newDesc };
            fetch(`${apiUrl}/${item.id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(updated)
            }).then(() => fetchItems());
        }
    });

    // Delete item
    deleteBtn.addEventListener("click", async () => {
        if (confirm("Delete this item?")) {
            await fetch(`${apiUrl}/${item.id}`, { method: "DELETE" });
            fetchItems();
        }
    });
}

// Add new item
form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const newItem = {
        title: titleInput.value,
        description: descriptionInput.value,
        completed: false
    };
    await fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newItem)
    });
    titleInput.value = "";
    descriptionInput.value = "";
    fetchItems();
});

// Initial load
fetchItems();
