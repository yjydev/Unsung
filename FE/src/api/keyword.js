import { apiInstance } from './index.js'

const api = apiInstance()

function keywordList (succes, fail) {
  api.get('api/news/search/wordcount').then(succes).catch(fail)
}

export default { keywordList }
