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
        path: ["/matches", "/tournaments"],
        permissions: ["participant"],
        constraints: "normal",
    },
    {
        path: ["/login", "/profile"],
        permissions: ["participant", "judge", "organizer"],
        constraints: "normal",
    },
    {
        path: ["/register/judge", "/register/information", "judge/score"],
        permissions: ["judge"],
        constraints: "normal",
    },
    {
        path: ["/tournaments/create", "/organizer/tournaments", "/matches"],
        permissions: ["organizer"],
        constraints: "normal",
    },
    {
        path: [ "/tournaments/:id", "/matches/:id"],
        permissions: ["organizer"],
        constraints: "edit",

    }
];

export default pages;
