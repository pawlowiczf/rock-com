const pages = [
    {
        path: [
            "/register",
            "/register/account-creating",
            "/register/chose-user-type",
        ],
        permissions: ["participant", "judge", "organizer"],
        constraints: "register",
    },
    {
        path: ["/login", "/profile", "/matches", "/tournaments"],
        permissions: ["participant", "judge", "organizer"],
        constraints: "normal",
    },
    {
        path: ["/register/judge", "/register/information", "judge/score"],
        permissions: ["judge"],
        constraints: "normal",
    },
    {
        path: ["/tournaments/create", "/tournaments/edit/:id", "/matches/edit"],
        permissions: ["organizer"],
        constraints: "normal",
    },
];

export default pages;
