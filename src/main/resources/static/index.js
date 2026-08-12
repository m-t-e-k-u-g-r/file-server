import {checkLogin, getHeaders} from "./auth.js";

let verified = false;
function updateUI() {
    const adminActions = document.getElementById("verified-actions");
    adminActions.style.display = verified ? 'block' : 'none';

    const verificationBlock = document.getElementById("verification");
    verificationBlock.style.display = verified ? 'none' : 'block';

    let statusDisplay = document.getElementById("status");
    statusDisplay.innerText = verified ? 'verified' : 'not verified';
}

verified = await checkLogin();
updateUI();

const fileId = new URLSearchParams(document.location.search).get("fileId");
if (fileId && fileId.trim() !== "") {
    document.getElementById("fileId").setAttribute("value", fileId);
}

const dashboardBtn = document.getElementById("dashboard-btn");
dashboardBtn.addEventListener("click", (e) => {
    window.open("/admin-ui/index.html")
});

const verifyForm = document.forms.verify;
verifyForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const formData = new FormData(verifyForm);
    const token = formData.get("token");

    const res = await fetch("auth/login", {
        method: "POST",
        headers: getHeaders(token),
    });

    if (res.ok) {
        const data = await res.json();
        sessionStorage.setItem("accessToken", data.accessToken);
        verified = true;
        updateUI();
    } else {
        sessionStorage.removeItem("accessToken");
        verified = false;
    }
});

const uploadForm = document.forms.upload;
uploadForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const fileInput = document.getElementById("file-upload");

    const formData = new FormData();
    formData.append("file", fileInput.files[0]);

    const res = await fetch("files", {
        method: "POST",
        headers: getHeaders(),
        body: formData
    });

    const errElement = document.getElementById("upload-error");
    if (res.ok) {
        errElement.innerText = "";

        document.getElementById("fileId").value = new URL(
            res.headers.get("Location")
        ).pathname.split("/").filter(Boolean).pop();
    } else {
        errElement.innerText = "Failed to upload file";
    }
});

const creationForm = document.forms.create;
creationForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const formData = new FormData(creationForm);
    const errElement = document.getElementById("creation-error");
    const fileId = formData.get("fileId").trim();
    if (fileId.length !== 36) {
        errElement.innerText = "Invalid UUID"
        return;
    } else { errElement.innerText = "" }
    const description = formData.get("description")?.trim() || null;

    const res = await fetch("keys/" + fileId, {
        method: "POST",
        headers: getHeaders(),
        body: description
    });

    if (!res.ok) {
        errElement.innerText = "Failed to generate access key";
        return;
    }
    const url = res.headers.get("Location");
    displayQRCode(url);
});

function displayQRCode(url) {
    new QRCode(document.getElementById("qrcode"), {
        text: url,
        width: 300,
        height: 300
    });
}
