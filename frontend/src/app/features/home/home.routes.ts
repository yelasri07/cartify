import { Routes } from "@angular/router";
import { FeedComponent } from "./feed/feed.component";
import { authGuard } from "../../core/guards/auth.guard";


export const routes: Routes = [
    {
        path: "",
        pathMatch: "full",
        redirectTo: "feed"
    },
    {
        path: 'profile/:id',
        canActivate: [authGuard],
        loadComponent: () => import("../profile/profile.component").then(m => m.ProfileComponent)
    },
    {
        path: "feed",
        component: FeedComponent
    }
]
