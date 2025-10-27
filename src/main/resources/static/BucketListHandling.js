const apiUrl = "http://localhost:8080/api/items";

const listEl = document.getElementById("list");
const form = document.getElementById("addForm");
const titleInput = document.getElementById("title");
const descriptionInput = document.getElementById("description");

// Fetch existing items from backend
async function fetchItems() {
    try {
        const response = await fetch(apiUrl);
        const items = await response.json();
        listEl.innerHTML = "";
        items.forEach(item => renderItem(item));
    } catch (error) {
        console.error("Failed to fetch items:", error);
    }
}

// Render a single item
function renderItem(item) {
    const li = document.createElement("li");
    li.className = "list-group-item d-flex flex-column flex-md-row align-items-start align-items-md-center justify-content-between";

    // Text container
    const textContainer = document.createElement("div");
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
