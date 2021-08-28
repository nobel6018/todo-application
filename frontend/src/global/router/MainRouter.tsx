import { BrowserRouter, Route } from "react-router-dom";
import Todo from "../../todo/page";

function MainRouter() {
    return (
        <BrowserRouter>
            <Route exact path="/" component={Todo} />
            <Route exact path="/todo" component={Todo} />
        </BrowserRouter>
    );
}

export default MainRouter;
