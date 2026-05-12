import { Routes } from "@angular/router";
import { FeedComponent } from "./feed/feed.component";


export const routes: Routes = [
    {
        path: "",
        pathMatch: "full",
        redirectTo: "feed"
    },
    {
        path: "feed",
        component: FeedComponent
    },

]
