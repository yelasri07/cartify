const URL = "https://localhost:8080"
export const API = {
    ME: `${URL}/users/me`,
    REGISTER: `${URL}/auth/register`,
    LOGIN: `${URL}/auth/login`,
    GET_POSTS: `${URL}/api/products`,
    PROFILE: `${URL}/users`,
    GET_PROFILE_POSTS: `${URL}/api/products/users`,
    CREATE_PRODUCT: `${URL}/api/products`,
    CREATE_MEDIA: `${URL}/media`,
    DELETE_PRODUCT: `${URL}/api/products`,
    UPDATE_PRODUCT: `${URL}/api/products`
}
