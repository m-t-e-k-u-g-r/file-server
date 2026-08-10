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
