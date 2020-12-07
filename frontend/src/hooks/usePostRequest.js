import {useCallback, useContext, useState} from 'react';
import UserContext from '../components/UserContext/component';

export default function usePostRequest(endpoint) { 
    const authedUser = useContext(UserContext)
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const doPost = useCallback((body, accessToken) => {
        setLoading(true)
        setError(null)
        setData(null);
        var bearerToken = ""
        if (accessToken) {
            bearerToken = accessToken
        } else if (authedUser) {
            bearerToken = authedUser.accessToken
        }
        fetch(endpoint, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': 'Bearer ' + bearerToken,
            },
            body: JSON.stringify(body),
        }).then(resp => {
            console.warn(resp)
            if (resp.ok === true) {
                resp.json().then(d => {
                    setData(d)
                    setLoading(false)
                })
            } else {
                setError(`Something went wrong when communicating with our servers (STATUS=${resp.status})`)
                setLoading(false)
            }
            
        }, error => {
            setError(error.toString());
            setLoading(false)
        })
    }, [endpoint, authedUser])

    return { doPost, data, error, loading }
}
