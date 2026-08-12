export function initUI() {
    const main = document.querySelector("main.dashboard-container");
    for (const config of ui_config) {
        const section = document.createElement("section");
        section.classList.add("table-section");
        const h2 = document.createElement("h2");
        h2.innerText = config.h2;

        const wrapper = document.createElement("div");
        wrapper.classList.add("table-wrapper");

        const table = document.createElement("table");
        table.id = config.table_id;

        const thead = document.createElement("thead");
        const tr = document.createElement("tr");
        const tbody = document.createElement("tbody");

        for (const row of config.headerRows) {
            const th = document.createElement("th");
            th.innerText = row;
            tr.appendChild(th);
        }

        thead.appendChild(tr);
        table.appendChild(thead);
        table.appendChild(tbody);
        wrapper.appendChild(table);
        section.appendChild(h2);
        section.appendChild(wrapper);
        main.appendChild(section);
    }
}

const ui_config = [
    { h2: 'Files', table_id: 'file-table', headerRows: ["Original Name", "Size", "Created at", "Action"] },
    { h2: 'Access Keys', table_id: 'key-table', headerRows: ["File", "Description", "Created at", "Status", "Action"] },
    { h2: 'Log Overview', table_id: 'log-table', headerRows: ["Access Time", "File", "Key", "Authorized"] },
];
