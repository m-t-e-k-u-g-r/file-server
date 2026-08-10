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
