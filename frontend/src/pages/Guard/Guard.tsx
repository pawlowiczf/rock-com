const pages = [
    {
        path: [
            "/register",
            "/register/account-creating",
            "/register/chose-user-type",
        ],
        permissions: ["participant", "referee", "organizer"],
        constraints: "register",
    },
    {
        path: [
            "/register/judge",
            "/register/information",
        ],
        permissions: ["referee", "organizer"],
        constraints: "register",
    },
    {
        path: ["/matches", "/tournaments"],
        permissions: ["participant"],
        constraints: "normal",
    },
    {
        path: ["/login", "/profile"],
        permissions: ["participant", "referee", "organizer"],
        constraints: "normal",
    },
    {
        path: ["/judge/score", "/judge/competition"],
        permissions: ["referee"],
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
