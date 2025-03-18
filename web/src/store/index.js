import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';
import ideStorage from './plugins/ideStorage';

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate) 
pinia.use(ideStorage)
 

export * from './modules'

export default pinia