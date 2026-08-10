let verified = false;
function updateUI() {
    const adminActions = document.getElementById("verified-actions");
    adminActions.style.display = verified ? 'block' : 'none';

    const verificationBlock = document.getElementById("verification");
    verificationBlock.style.display = verified ? 'none' : 'block';

    let statusDisplay = document.getElementById("status");
    statusDisplay.innerText = verified ? 'verified' : 'not verified';
}

fetch("auth", {
    method: "POST",
    headers: {"Authorization": sessionStorage.getItem("adminKey")}
}).then(r => {
    if (r.ok) {
        verified = true;
    }
    updateUI();
});

const verifyForm = document.forms.verify;
verifyForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const formData = new FormData(verifyForm);
    const token = formData.get("token");

    const res = await fetch("auth", {
        method: "POST",
        headers: {
            "Authorization": token,
        },
    });

    if (res.ok) {
        sessionStorage.setItem("adminKey", token.toString());
        verified = true;
        updateUI();
    } else {
        sessionStorage.setItem("adminKey", null);
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
        headers: {
            "Authorization": sessionStorage.getItem("adminKey")
        },
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
