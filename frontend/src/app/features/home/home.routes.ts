import { Routes } from "@angular/router";
import { FeedComponent } from "./feed/feed.component";


export const routes: Routes = [
    {
        path: "",
        pathMatch: "full",
        redirectTo: "feed"
    },
    {
        path: 'profile/:id',
        loadComponent: () => import("../profile/profile.component").then(m => m.ProfileComponent)
    },
    {
        path: "feed",
        component: FeedComponent
    }
]
