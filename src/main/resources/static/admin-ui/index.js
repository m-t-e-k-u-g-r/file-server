import {checkLogin} from "../auth.js";
import {createKey, deleteFile, fetchRes, mapValues, revokeKey} from "./utils.js";
import {initUI} from "./init-template.js";

if (await checkLogin()) {
    initUI();
    await fetchAll();
}

document.getElementById("return").addEventListener("click", () => {
    window.location.href = "/index.html";
});

export async function fetchAll() {
    const resourceConfig = [
        { key: "file", link: "/admin/files", values: ["originalFilename",
                { name: "size", type: "bytes"},
                { name: "createdAt", type: "date" }
            ],
            actions: [
                { label: 'Create Key', class: 'btn-create', fn: createKey },
                { label: 'Delete', class: 'btn-delete', fn: deleteFile },
            ]
        },
        { key: "key", link: "/admin/keys",
            values: ["fileName", "description", { name: "createdAt", type: "date" }, "status"],
            actions: [
                { label: 'Revoke', class: 'btn-revoke', fn: revokeKey },
            ]
        },
        { key: "log", link: "/admin/logs",
            values: [{ name: "accessTime", type: "date" }, "fileName", "keyDescription",
                { name: "authorized", type: "boolean" }
            ]
        }
    ];
    await Promise.all(
        resourceConfig.map(async (config) => {
            const data = await fetchRes(config.link);
            await loadData(data, config.values, config.key, config.actions);
        })
    );
}

async function loadData(data, values, key, actions) {
    for (const d of data) {
        const row = document.createElement("tr");
        for (const v of values) {
            insertCell(v, d, row);
        }
        if (actions) {
            const btnCell = document.createElement("td");
            for (const action of actions) {
                addActionCell(action, d, btnCell);
            }
            row.appendChild(btnCell);
        }
        const table = document.getElementById(key + "-table");
        table.appendChild(row);
    }
}

function insertCell(v, d, row) {
    const cell = document.createElement("td");
    let value;
    switch (typeof v) {
        case "object":
            value = mapValues(v, d);
            break;
        default:
            value = d[v];
    }
    cell.textContent = value;
    row.appendChild(cell);
}

function addActionCell(action, d, btnCell) {
    const btn = document.createElement("button");
    btn.classList.add(action.class);
    btn.innerText = action.label;
    btn.addEventListener("click", () => {
        action.fn(d.id)
    });

    btn.disabled = (action.fn === revokeKey && d.status === "Revoked")

    btnCell.appendChild(btn);
}