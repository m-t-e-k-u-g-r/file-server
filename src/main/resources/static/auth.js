export const getHeaders = (key) => {
    const accessKey = key ?? sessionStorage.getItem("accessToken");
    return new Headers({
        "Authorization": "Bearer " + accessKey
    });
};

export async function checkLogin() {
    return (await
        fetch("/auth/check", {
            method: "POST",
            headers: getHeaders()
        })
    ).ok;
}