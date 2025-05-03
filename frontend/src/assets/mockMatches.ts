export const matches = [
  {
    id: 1,
    name: "Match 1",
    nextMatchId: 5,
    tournamentRoundText: "1",
    startTime: "2025-05-12",
    state: "DONE",
    participants: [
      { id: "p1", name: "Player 1", resultText: "1", isWinner: false, status: "PLAYED" },
      { id: "p2", name: "Player 2", resultText: "3", isWinner: true, status: "PLAYED" },
    ],
  },
  {
    id: 2,
    name: "Match 2",
    nextMatchId: 5,
    tournamentRoundText: "1",
    startTime: "2025-05-12",
    state: "DONE",
    participants: [
      { id: "p3", name: "Player 5", resultText: "3", isWinner: true, status: "PLAYED" },
      { id: "p4", name: "Player 6", resultText: "2", isWinner: false, status: "PLAYED" },
    ],
  },
  {
    id: 5,
    name: "Match 5",
    nextMatchId: null,
    tournamentRoundText: "2",
    startTime: "2025-05-13",
    state: "DONE",
    participants: [
      { id: "p2", name: "Player 2", resultText: "2", isWinner: false, status: "PLAYED" },
      { id: "p3", name: "Player 5", resultText: "3", isWinner: true, status: "PLAYED" },
    ],
  },
];
