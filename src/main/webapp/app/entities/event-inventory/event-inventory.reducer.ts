import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IEventInventory, defaultValue } from 'app/shared/model/event-inventory.model';

export const ACTION_TYPES = {
  FETCH_EVENTINVENTORY_LIST: 'eventInventory/FETCH_EVENTINVENTORY_LIST',
  FETCH_EVENTINVENTORY: 'eventInventory/FETCH_EVENTINVENTORY',
  CREATE_EVENTINVENTORY: 'eventInventory/CREATE_EVENTINVENTORY',
  UPDATE_EVENTINVENTORY: 'eventInventory/UPDATE_EVENTINVENTORY',
  DELETE_EVENTINVENTORY: 'eventInventory/DELETE_EVENTINVENTORY',
  RESET: 'eventInventory/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEventInventory>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type EventInventoryState = Readonly<typeof initialState>;

// Reducer

export default (state: EventInventoryState = initialState, action): EventInventoryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EVENTINVENTORY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EVENTINVENTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EVENTINVENTORY):
    case REQUEST(ACTION_TYPES.UPDATE_EVENTINVENTORY):
    case REQUEST(ACTION_TYPES.DELETE_EVENTINVENTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EVENTINVENTORY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EVENTINVENTORY):
    case FAILURE(ACTION_TYPES.CREATE_EVENTINVENTORY):
    case FAILURE(ACTION_TYPES.UPDATE_EVENTINVENTORY):
    case FAILURE(ACTION_TYPES.DELETE_EVENTINVENTORY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EVENTINVENTORY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_EVENTINVENTORY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EVENTINVENTORY):
    case SUCCESS(ACTION_TYPES.UPDATE_EVENTINVENTORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EVENTINVENTORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/event-inventories';

// Actions

export const getEntities: ICrudGetAllAction<IEventInventory> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_EVENTINVENTORY_LIST,
  payload: axios.get<IEventInventory>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IEventInventory> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EVENTINVENTORY,
    payload: axios.get<IEventInventory>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEventInventory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EVENTINVENTORY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEventInventory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EVENTINVENTORY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEventInventory> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EVENTINVENTORY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
