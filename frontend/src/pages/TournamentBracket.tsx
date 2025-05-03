import { ReactNode } from "react";
import {
  SingleEliminationBracket,
  Match,
  SVGViewer,
} from "@g-loot/react-tournament-brackets";
import { matches } from "../assets/mockMatchesHuge"; // plik z danymi meczów
import "../styles//TournamentBracket.css"; // import pliku CSS

type SVGWrapperProps = {
  children: ReactNode;
} & React.ComponentProps<"div">;

const TournamentBracket = () => {
    const screenWidth = window.innerWidth * .9;
  
    return (
      <div className="container">
        <div className="header">
          <h2 className="title">Wielki Tenis</h2>
          <p className="date">12.05.2025–14.05.2025</p>
        </div>
        <div className="bracket">
          <SingleEliminationBracket
            matches={matches}
            matchComponent={Match}
            svgWrapper={({ children, ...props }: SVGWrapperProps) => (
              <SVGViewer width={screenWidth} {...props}>
                {children}
              </SVGViewer>
            )}
          />
        </div>
      </div>
    );
  };
  

export default TournamentBracket;
