import {fetchAll} from "./index.js";
import {getHeaders} from "../auth.js";

export function mapValues(tableValue, dataEntry) {
    switch (tableValue.type) {
        case "bytes":
            return formatBytes(dataEntry[tableValue.name])
        case "date":
            return formatUTC(dataEntry[tableValue.name]);
        case "boolean":
            return dataEntry[tableValue.name] ? "Yes" : "No";
        default:
            return dataEntry[tableValue.name];
    }
}

function formatUTC(isoString) {
    const date = new Date(isoString);
    return date.toISOString().replace('T', ' ').substring(0, 19);
}

function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';

    const k = 1000;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];

    const i = Math.floor(Math.log(bytes) / Math.log(k));
    const formattedValue = parseFloat((bytes / Math.pow(k, i)).toFixed(decimals));

    return `${formattedValue} ${sizes[i]}`;
}

export async function fetchRes(link) {
    const response = await fetch(link, { headers: getHeaders() });
    if (response.ok) return await response.json();
    return [];
}

export function createKey(id) {
    window.location.href = "/index.html?fileId=" + id;
}

export function deleteFile(id) {
    const route = "/files/" + id;
    deleteRessource(route);
}

export function revokeKey(id) {
    const route = "/keys/" + id;
    deleteRessource(route);
}

function deleteRessource(route) {
    fetch(route, {
        method: "DELETE",
        headers: getHeaders()
    }).then(() => {
        fetchAll()
    });
}