import {useCallback, useContext, useEffect, useState} from 'react';
import UserContext from '../components/UserContext/component';

export default function useGetRequest(endpoint) { 
    const authedUser = useContext(UserContext)
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const refetch = useCallback((accessToken) => {
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
            headers: {
              'Authorization': 'Bearer ' + bearerToken,
            },
        }).then(resp => {
            console.warn(resp)
            if (resp.ok === true) {
                resp.json().then(v => {
                    setData(v)
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

    useEffect(() => {
        refetch()
    }, [endpoint, refetch])

    return { data, error, loading, refetch }
}